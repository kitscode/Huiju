//TO DO:
//
// - improve timer performance (especially on Eee Pad)
// - improve child rearranging

package kevinz.huiju.utils.rearrange_tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class DraggableGridView extends ViewGroup implements
		View.OnTouchListener, View.OnClickListener, View.OnLongClickListener {
	// layout vars
	public static float childRatio = .9f;
	protected int colCount, childSize, padding, dpi;
	protected float lastDelta = 0;
	protected Handler handler = new Handler();
	// dragging vars
	protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
	protected boolean enabled = true, touching = false;
	// anim vars
	public static int animT = 150;
	protected ArrayList<Integer> newPositions = new ArrayList<Integer>();
	// listeners
	protected OnRearrangeListener onRearrangeListener;
	protected OnClickListener secondaryOnClickListener;
	private OnItemClickListener onItemClickListener;
	private int itemwidth = 0;
	private int xlen = 0;
	private int ylen = 0;

	// CONSTRUCTOR AND HELPERS
	public DraggableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setListeners();
		setChildrenDrawingOrderEnabled(true);

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		dpi = metrics.densityDpi;
	}

	protected void setListeners() {
		setOnTouchListener(this);
		super.setOnClickListener(this);
		setOnLongClickListener(this);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		secondaryOnClickListener = l;
	}

	// OVERRIDES
	@Override
	public void addView(View child) {
		super.addView(child);
		newPositions.add(-1);
	};

	@Override
	public void removeViewAt(int index) {
		super.removeViewAt(index);
		newPositions.remove(index);
	};

	// LAYOUT
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// compute width of view, in dp
		// float w = (r - l) / (dpi / 160f);

		// determine number of columns, at least 2
		colCount = 4;
		// int sub = 240;
		// w -= 280;
		// while (w > 0) {
		// colCount++;
		// w -= sub;
		// sub += 40;
		// }

		// determine childSize and padding, in px
		childSize = (r - l) / colCount;
		childSize = Math.round(childSize * childRatio);
		itemwidth = childSize / 2;
		padding = ((r - l) - (childSize * colCount)) / (colCount + 1);

		for (int i = 0; i < getChildCount(); i++)
			if (i != dragged) {
				Point xy = getCoorFromIndex(i);
				getChildAt(i).layout(xy.x, xy.y, xy.x + childSize,
						xy.y + itemwidth);
			}
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (dragged == -1)
			return i;
		else if (i == childCount - 1)
			return dragged;
		else if (i >= dragged)
			return i + 1;
		return i;
	}

	public int getIndexFromCoor(int x, int y) {
		int col = getColFromCoor(x), row = getRowFromCoor(y);
		if (col == -1 || row == -1) // touch is between columns or rows
			return -1;
		int index = row * colCount + col;
		if (index >= getChildCount())
			return -1;
		return index;
	}

	protected int getColOrRowFromCoor(int coor) {
		coor -= padding;
		for (int i = 0; coor > 0; i++) {
			if (coor < childSize)
				return i;
			coor -= (childSize + padding);
		}
		return -1;
	}

	protected int getColFromCoor(int col) {
		col -= padding;
		for (int i = 0; col > 0; i++) {
			if (col < childSize)
				return i;
			col -= (childSize + padding);
		}
		return -1;
	}

	protected int getRowFromCoor(int row) {
		row -= padding;
		for (int i = 0; row > 0; i++) {
			if (row < itemwidth)
				return i;
			row -= (itemwidth + padding);
		}
		return -1;
	}

	protected int getTargetFromCoor(int x, int y) {
		if (getColOrRowFromCoor(y) == -1) // touch is between rows
			return -1;
		// if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
		// return -1;

		int leftPos = getIndexFromCoor(x - (childSize / 4), y);
		int rightPos = getIndexFromCoor(x + (childSize / 4), y);
		if (leftPos == -1 && rightPos == -1) // touch is in the middle of
												// nowhere
			return -1;
		if (leftPos == rightPos) // touch is in the middle of a visual
			return -1;

		int target = -1;
		if (rightPos > -1)
			target = rightPos;
		else if (leftPos > -1)
			target = leftPos + 1;
		if (dragged < target)
			return target - 1;

		// Toast.makeText(getContext(), "Target: " + target + ".",
		// Toast.LENGTH_SHORT).show();
		return target;
	}

	protected Point getCoorFromIndex(int index) {
		int col = index % colCount;
		int row = index / colCount;
		return new Point(padding + (childSize + padding) * col, padding
				+ (itemwidth + padding) * row);
	}

	public int getIndexOf(View child) {
		for (int i = 0; i < getChildCount(); i++)
			if (getChildAt(i) == child)
				return i;
		return -1;
	}

	// EVENT HANDLERS
	public void onClick(View view) {
		if (enabled) {
			if (secondaryOnClickListener != null)
				secondaryOnClickListener.onClick(view);
			if (onItemClickListener != null && getLastIndex() != -1)
				onItemClickListener.onItemClick(null,
						getChildAt(getLastIndex()), getLastIndex(),
						getLastIndex() / colCount);
		}
	}

	public boolean onLongClick(View view) {
		if (!enabled)
			return false;
		int index = getLastIndex();
		if (index != -1) {
			dragged = index;
			animateDragged();
			return true;
		}
		return false;
	}

	public boolean onTouch(View view, MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			enabled = true;
			lastX = (int) event.getX();
			lastY = (int) event.getY();
			int lastindex = getLastIndex();

			if (lastindex != -1) {
				View item = getChildAt(lastindex);
				xlen = lastX - (int) item.getX();
				ylen = lastY - (int) item.getY();
			}

			touching = true;
			break;
		case MotionEvent.ACTION_MOVE:
			int delta = lastY - (int) event.getY();
			if (dragged != -1) {
				// change draw location of dragged visual
				int x = (int) event.getX(), y = (int) event.getY();
				getChildAt(dragged).layout(x - xlen, y - ylen,
						x - xlen + childSize, y - ylen + itemwidth);

				// check for new target hover
				int target = getTargetFromCoor(x, y);
				if (lastTarget != target) {
					if (target != -1) {
						animateGap(target);
						lastTarget = target;
					}
				}
			} else {
				// scroll += delta;
				// clampScroll();
				// if (Math.abs(delta) > 2)
				// enabled = false;
				// onLayout(true, getLeft(), getTop(), getRight(), getBottom());
			}
			lastX = (int) event.getX();
			lastY = (int) event.getY();
			lastDelta = delta;
			break;
		case MotionEvent.ACTION_UP:
			if (dragged != -1) {
				View v = getChildAt(dragged);
				if (lastTarget != -1)
					reorderChildren();
				else {
					Point xy = getCoorFromIndex(dragged);
					v.layout(xy.x, xy.y, xy.x + childSize, xy.y + itemwidth);
				}
				v.clearAnimation();
				if (v instanceof ImageView)
					((ImageView) v).setAlpha(255);
				lastTarget = -1;
				dragged = -1;
			}
			touching = false;
			break;
		}
		if (dragged != -1)
			return true;
		return false;
	}

	// EVENT HELPERS
	protected void animateDragged() {
		View v = getChildAt(dragged);
		// int x = getCoorFromIndex(dragged).x + childSize / 2, y =
		// getCoorFromIndex(dragged).y
		// + childSize / 2;
		// int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
		// v.layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(.667f, 1, .667f, 1,
				childSize * 3 / 4, childSize * 3 / 4);
		scale.setDuration(animT);
		AlphaAnimation alpha = new AlphaAnimation(1, .5f);
		alpha.setDuration(animT);

		animSet.addAnimation(scale);
		animSet.addAnimation(alpha);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);

		v.clearAnimation();
		v.startAnimation(animSet);
	}

	protected void animateGap(int target) {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (i == dragged)
				continue;
			int newPos = i;
			if (dragged < target && i >= dragged + 1 && i <= target)
				newPos--;
			else if (target < dragged && i >= target && i < dragged)
				newPos++;

			// animate
			int oldPos = i;
			if (newPositions.get(i) != -1)
				oldPos = newPositions.get(i);
			if (oldPos == newPos)
				continue;

			Point oldXY = getCoorFromIndex(oldPos);
			Point newXY = getCoorFromIndex(newPos);
			Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y
					- v.getTop());
			Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y
					- v.getTop());

			TranslateAnimation translate = new TranslateAnimation(
					Animation.ABSOLUTE, oldOffset.x, Animation.ABSOLUTE,
					newOffset.x, Animation.ABSOLUTE, oldOffset.y,
					Animation.ABSOLUTE, newOffset.y);
			translate.setDuration(animT);
			translate.setFillEnabled(true);
			translate.setFillAfter(true);
			v.clearAnimation();
			v.startAnimation(translate);

			newPositions.set(i, newPos);
		}
	}

	@SuppressLint("WrongCall")
	protected void reorderChildren() {
		// FIGURE OUT HOW TO REORDER CHILDREN WITHOUT REMOVING THEM ALL AND
		// RECONSTRUCTING THE LIST!!!
		if (onRearrangeListener != null)
			onRearrangeListener.onRearrange(dragged, lastTarget);
		ArrayList<View> children = new ArrayList<View>();
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).clearAnimation();
			children.add(getChildAt(i));
		}
		removeAllViews();
		while (dragged != lastTarget)
			if (lastTarget == children.size()) // dragged and dropped to the
												// right of the last element
			{
				children.add(children.remove(dragged));
				dragged = lastTarget;
			} else if (dragged < lastTarget) // shift to the right
			{
				Collections.swap(children, dragged, dragged + 1);
				dragged++;
			} else if (dragged > lastTarget) // shift to the left
			{
				Collections.swap(children, dragged, dragged - 1);
				dragged--;
			}
		for (int i = 0; i < children.size(); i++) {
			newPositions.set(i, -1);
			addView(children.get(i));
		}
		onLayout(true, getLeft(), getTop(), getRight(), getBottom());
	}

	public int getLastIndex() {
		return getIndexFromCoor(lastX, lastY);
	}

	// OTHER METHODS
	public void setOnRearrangeListener(OnRearrangeListener l) {
		this.onRearrangeListener = l;
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		this.onItemClickListener = l;
	}
}
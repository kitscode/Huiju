package kevinz.huiju.ui.video;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import kevinz.huiju.R;


public class PalyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        VideoView videoView = (VideoView)findViewById(R.id.video_view);
        String mp4_url = getIntent().getStringExtra("mp4_url");
        Uri uri = Uri.parse(mp4_url);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
        videoView.setVideoURI(uri);
        videoView.start();
    }
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override    public void onCompletion(MediaPlayer mp) {
            Toast.makeText(PalyActivity.this, "播放完毕", Toast.LENGTH_SHORT).show();
        }
    }
}

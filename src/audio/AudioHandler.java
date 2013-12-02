package audio;

import java.util.HashMap;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class AudioHandler {
	private SoundPool sound;
	private int soundMax;
	private float streamVolume;
	private MediaPlayer music;
	private HashMap<Integer, Integer> audioPool;
	public AudioManager audioManager;
	private Context context;
	
	public AudioHandler(Context theContext) {
	    context = theContext;
	    soundMax = 3; //this sets the number of maximum available overlaying sounds
	    audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
	    		audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
	    sound = new SoundPool(soundMax, AudioManager.STREAM_MUSIC, 0);
	    audioPool = new HashMap<Integer, Integer>(); //stores the audio id into a hashmap
	    music = new MediaPlayer();
	    init();
	}
	public void init(){
		//add sound effects
	    //addSound(0,R.raw.sound0);
	    //addSound(1,R.raw.sound1);
	}
	public void addSound(int index, int SoundID) {   
		//may take a while depending on file size
	    int streamID = -1;
	    audioPool.put(index, sound.load(context, SoundID, 1));
	    do {streamID = sound.play(audioPool.get(index), 0, 0, 1, 0, 1f);}
	    while(streamID==0);
	}
	public void playSound(int index) {
		streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    sound.play((Integer) audioPool.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
	public void stopSound(int index){
		sound.stop(index);
	}
	public void playMusic(int id) {
		music = MediaPlayer.create(context, id);
		music.setVolume(0.2f, 0.2f); //nice quiet background
		music.setLooping(true);
		music.start();
	}
	public void toggleMusic(){ 
		if (music.isPlaying()) music.pause();
		else music.start();
	}
	public void stopMusic(){
		music.stop();
		music.release();
	}
	public void stopAll(){ //stops all audio
		for (int i = 0; i < audioPool.size(); i++){
			sound.stop(i);
		}
		music.stop();
	}
}
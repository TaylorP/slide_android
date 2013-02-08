package net.orangebytes.slide.utils;

import net.orangebytes.slide.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class Sounds {
	private static Sounds sSounds;
	
	private SoundPool mSoundPool = null;
	private int		  mLoaded = 0;
	private int[]	  mSounds;		
	
	private Sounds(Context pContext) {
	    mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
	      @Override
	      public void onLoadComplete(SoundPool soundPool, int sampleId,
	          int status) {
	        mLoaded++;
	      }
	    });
	    mSounds = new int[3];
	    
	    mSounds[0] = mSoundPool.load(pContext, R.raw.sound0, 1);
	    mSounds[1] = mSoundPool.load(pContext, R.raw.sound1, 1);
	    mSounds[2] = mSoundPool.load(pContext, R.raw.sound2, 1);
	}
	
	public void playSound(Context pContext) {
	      AudioManager audioManager = (AudioManager) pContext.getSystemService(Context.AUDIO_SERVICE);
	      float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	      float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	      float volume = actualVolume / maxVolume;
	      
	      if (mLoaded >= 3) {
	        mSoundPool.play((int)(Math.random()*3), volume, volume, 1, 0, 1f);
	      }
	}
	
	public static Sounds get(Context pContext) {
		if(sSounds == null){
			sSounds = new Sounds(pContext);
		}
		return sSounds;
	}
}
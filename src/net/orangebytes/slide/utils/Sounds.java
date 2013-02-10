package net.orangebytes.slide.utils;

import net.orangebytes.slide.R;
import net.orangebytes.slide.preferences.GamePreferences;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.util.Log;

public class Sounds {
	private static Sounds sSounds;

	private MediaPlayer mMusic = null;
	private SoundPool mSoundPool = null;
	private boolean mPlaying = false;
	private int mLoaded = 0;
	private int[] mSounds;

	private Sounds(Context pContext) {
		mMusic = MediaPlayer.create(pContext, R.raw.music);
		mMusic.setLooping(true);
		mMusic.setVolume(0.5f, 0.5f);
		
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				mLoaded++;
			}
		});
		mSounds = new int[3];

		mSounds[0] = mSoundPool.load(pContext, R.raw.sound0, 1);
		mSounds[1] = mSoundPool.load(pContext, R.raw.sound1, 1);
		mSounds[2] = mSoundPool.load(pContext, R.raw.sound2, 1);
	}

	public void playSound(Context pContext) {

		if(!GamePreferences.get(pContext).getSoundsPreference())
			return;
		
		AudioManager audioManager = (AudioManager) pContext.getSystemService(Context.AUDIO_SERVICE);
		
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;

		if (mLoaded >= 3) {
			mSoundPool.play(mSounds[(int) (Math.random() * 3)], volume, volume,1, 0, 1f);
		}
	}
	
	public void startMusic(Context pContext) {
		if(!mPlaying) {
			mMusic.start();
			mPlaying = true;
		}
	}
	
	public void stopMusic(Context pContext) {
		if(mPlaying) {
			mMusic.pause();
			mPlaying = false;
		}
	}

	public static Sounds get(Context pContext) {
		if (sSounds == null) {
			sSounds = new Sounds(pContext);
		}
		return sSounds;
	}
}
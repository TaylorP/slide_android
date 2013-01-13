package net.orangebytes.slide.adapters;

import net.orangebytes.slide.R;
import net.orangebytes.slide.model.PuzzleInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OptionsListAdapter extends ArrayAdapter<PuzzleInfo> {
  private final Context 		mContext;
  private final PuzzleInfo[] 	mValues;
  private final Typeface 		mTypeface;
  

  public OptionsListAdapter(Context context, PuzzleInfo[] values) {
    super(context,R.layout.puzzle_info,values);
    mContext = context;
    mValues = values;
    mTypeface = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Light.ttf");
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = inflater.inflate(R.layout.puzzle_info, parent, false);
 
    TextView titleText = (TextView) row.findViewById(R.id.title_text);
    TextView timeText = (TextView) row.findViewById(R.id.time_text);
    TextView moveText = (TextView) row.findViewById(R.id.move_text);
    ImageView puzzleThumb = (ImageView) row.findViewById(R.id.puzzle_thumb);
    
    if(titleText != null) {
        titleText.setText(mValues[position].getTitle());
        titleText.setTypeface(mTypeface);
        
        int puzzleTime = mValues[position].getTime();

        if(puzzleTime < 10)
        	timeText.setText("0:0" + puzzleTime);
        else if (puzzleTime < 60)
        	timeText.setText("0:" + puzzleTime);
        else {
        	if(puzzleTime % 60 < 10)
        		timeText.setText(puzzleTime/60 + ":0" + puzzleTime%60);
        	else
        		timeText.setText(puzzleTime/60 + ":" + puzzleTime%60);
        }
        
        moveText.setText(mValues[position].getMoves() + "");
	
    }
    
    Resources res = mContext.getResources();
    int resID = res.getIdentifier(mValues[position].getThumb(), "drawable", mContext.getPackageName());
    Drawable drawable = res.getDrawable(resID);
    puzzleThumb.setImageDrawable(drawable );
    
    return row;
  }
} 
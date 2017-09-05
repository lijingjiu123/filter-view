package cn.lijingjiu.filterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class FilterView extends View {
    private int addColor,tagColor,tagCancleColor;
    private float dividWidth,dividHeight,tagHeight,iconSize, textSize;
    private String addIcon,cancleIcon;
    private Paint paint;
    private TextPaint textPaint,iconPaint;
    private ArrayList<String> filterList = null;
    private IconClickListener listener = null;
    private Rect r;
    private List<PointF> tagsList;
    private int w;

    public FilterView(Context context) {
        this(context,null);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FilterView);
        addColor = ta.getColor(R.styleable.FilterView_fv_add_icon_color, Color.MAGENTA);
        tagColor = ta.getColor(R.styleable.FilterView_fv_tag_color, Color.GREEN);
        tagCancleColor = ta.getColor(R.styleable.FilterView_fv_tag_cancle_color, Color.GRAY);
        dividWidth = ta.getDimension(R.styleable.FilterView_fv_tag_divid_width,10);
        dividHeight = ta.getDimension(R.styleable.FilterView_fv_tag_divid_height,10);
        tagHeight = ta.getDimension(R.styleable.FilterView_fv_tag_height,10);
        iconSize = ta.getDimension(R.styleable.FilterView_fv_icon_size,10);
        textSize = ta.getDimension(R.styleable.FilterView_fv_text_size,10);
        addIcon = ta.getString(R.styleable.FilterView_fv_add_icon);
        cancleIcon = ta.getString(R.styleable.FilterView_fv_tag_cancle_icon);
        ta.recycle();
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //
        iconPaint = new TextPaint();
        iconPaint.setTextSize(iconSize);
        iconPaint.setColor(Color.WHITE);
        iconPaint.setAntiAlias(true);
        //
        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        //
        r = new Rect();
        tagsList = new ArrayList<>();
    }

    public void setData(ArrayList<String> filterList){
        if (filterList == null) {
            this.filterList = filterList;
        }else{
            this.filterList = filterList;
            requestLayout();
        }
        invalidate();
    }

    public void setTypeface(Typeface iconTypeface, Typeface textTypeface){
        iconPaint.setTypeface(iconTypeface);
        textPaint.setTypeface(textTypeface);
        postInvalidate();
    }

    public void setIconClickListener(IconClickListener l) {
        listener = l;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = w = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;

        if (filterList == null || filterList.size() == 0){
            height = (int) tagHeight;
        }else {
           height = calculateTagPosition(width);
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        //画加号按钮
        paint.setColor(addColor);
        canvas.drawRect(w-tagHeight,0,w,tagHeight,paint);
        iconPaint.getTextBounds(addIcon,0,addIcon.length(),r);
        canvas.drawText(addIcon,w-tagHeight/2f-r.width()/2f,tagHeight/2f+r.height()/2f,iconPaint);
        if (filterList == null || filterList.size() == 0) return;
        if (tagsList.size() == 0) return;
        //画标签
        for (int i = 0; i < tagsList.size();i++){
            PointF pf = tagsList.get(i);
            String s = filterList.get(i);
            textPaint.getTextBounds(s,0,s.length(),r);
            float xStart = pf.x;
            float xEnd =pf.x+2*dividWidth+r.width();
            float yStart = pf.y-tagHeight;
            float yEnd =pf.y;
            paint.setColor(tagColor);
            canvas.drawRect(xStart,yStart,xEnd,yEnd,paint);
            paint.setColor(tagCancleColor);
            canvas.drawRect(xEnd,yStart,xEnd+tagHeight,yEnd,paint);
            canvas.drawText(s,(xStart+xEnd)/2-r.width()/2f,(yStart+yEnd)/2+r.height()/2f,textPaint);
            iconPaint.getTextBounds(cancleIcon,0,cancleIcon.length(),r);
            canvas.drawText(cancleIcon,xEnd+tagHeight/2f-r.width()/2f,(yStart+yEnd)/2+r.height()/2f,iconPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(MotionEvent.ACTION_DOWN == event.getAction()){
            float x = event.getX();
            float y = event.getY();
            if (w-tagHeight <= x && x <= w && 0 <= y && y <= tagHeight){
                listener.onIconClick();
            }else if(filterList != null && filterList.size() != 0 ){
                for(int i = 0; i < tagsList.size(); i++){
                    PointF pf = tagsList.get(i);
                    String s = filterList.get(i);
                    textPaint.getTextBounds(s,0,s.length(),r);
                    float xStart = pf.x+2*dividWidth+r.width();
                    float xEnd = xStart +tagHeight;
                    float yStart = pf.y-tagHeight;
                    float yEnd =pf.y;
                    if (xStart <= x && x <= xEnd && yStart <= y && y <= yEnd){
                        filterList.remove(i);
                        listener.onItemDelete();
                        requestLayout();
                        invalidate();
                        return true;
                    }
                }
            }

        }

        return super.onTouchEvent(event);
    }

    private int calculateTagPosition(int width){
        int w = width;
        tagsList.clear();
        int rowNum = 1;
        float sumWidth =0;
        int gap = (int)(tagHeight+dividWidth*3);
        for (int i = 0; i < filterList.size();i++){
            String s = filterList.get(i);
            textPaint.getTextBounds(s,0,s.length(),r);
            sumWidth += r.width()+gap;
            if (rowNum == 1){
                if (sumWidth > w-tagHeight){
                    rowNum += 1;
                    sumWidth = r.width()+gap;
                    tagsList.add(new PointF(sumWidth - (r.width()+gap),rowNum * tagHeight+dividHeight));
                }else{
                    tagsList.add(new PointF(sumWidth - (r.width()+gap),tagHeight));
                }
            }else{
                if (sumWidth > w + dividWidth){
                    rowNum += 1;
                    sumWidth = r.width()+gap;
                    tagsList.add(new PointF(sumWidth - (r.width()+gap),rowNum * tagHeight+(rowNum-1)*dividHeight));
                }else {
                    tagsList.add(new PointF(sumWidth - (r.width()+gap),rowNum * tagHeight+(rowNum-1)*dividHeight));
                }
            }
        }

        return (int)(rowNum * tagHeight+(rowNum-1)*dividHeight);
    }

    public interface IconClickListener {
        void onIconClick();
        void onItemDelete();
    }
}

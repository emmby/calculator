package roboguice.calculator.util;

import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;

import android.content.SharedPreferences;

import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Stack;

/**
 * A Stack of numbers that can be operated on.
 *
 * In addition to maintaining a stack, this class also maintains an accumulator register that
 * can be used to keep track of the digits that a user is typing up until they hit 'enter'
 * to push the accumulated digits onto the stack.
 *
 * Persists itself to shared preferences.
 *
 * Is a singleton.
 */
@ContextSingleton
public class RpnStack extends Stack<BigDecimal> {
    @Inject SharedPreferences prefs;

    String digitAccumulator = "";




    public String getDigitAccumulator() {
        return digitAccumulator;
    }

    public void setDigitAccumulator(String digitAccumulator) {
        this.digitAccumulator = digitAccumulator;
    }

    public void appendToDigitAccumulator(CharSequence text) {
        digitAccumulator += text;
    }

    public void pushDigitAccumulatorOnStack() {
        if( digitAccumulator.length()>0 ) {
            push(new BigDecimal(digitAccumulator));
            digitAccumulator="";
        }
    }






    /**
     * Save to prefs automatically on pause
     */
    protected void onPause( @Observes OnPauseEvent ignored) {
        Ln.d("RpnStack.onPause");
        final SharedPreferences.Editor edit = prefs.edit();

        edit.clear();

        for( int i=0; i<size(); ++i )
            edit.putString(String.valueOf(i), get(i).toString());

        edit.commit();
    }

    /**
     * Restore from prefs automatically on resume
     */
    protected void onResume( @Observes OnResumeEvent ignored ) {
        Ln.d("RpnStack.onResume");
        for( int i=0; prefs.contains(String.valueOf(i)); ++i)
            insertElementAt( new BigDecimal(prefs.getString(String.valueOf(i),null)), i);
    }



}

package com.example.rbeserra.todolist.content;

import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by renato on 9/6/15.
 */
public class CalendarUtils {
    public static final int OFFSET_HOUR = 2;
    private static String TAG = "CalendarUtils";
    private static final Pattern HOUR_OF_DAY_PATTERN = Pattern.compile("(\\d+):(\\d{2})");
    private static final Pattern HOUR_PATTERN = Pattern.compile("(\\d{1,2})\\s*([a,p]m)");


    /**
     * Creates the intent for a Calendar event for a given task text.
     * Tries to parse event begin time in the formats HH:mm or HH[pm/am]
     * The default time is 2 hours ahead of the current time.
     *
     * @param taskText the task text
     * @return the intent to create the calendar event.
     */
    public static Intent getIntent(CharSequence taskText) {
        Calendar beginTime = Calendar.getInstance();
        if (!setHour(taskText, beginTime) && !setHourOfDay(taskText, beginTime)) {
            beginTime.add(Calendar.HOUR, OFFSET_HOUR);
        }
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, taskText)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        return intent;
    }

    private static boolean setHour(CharSequence text, Calendar time) {
        Matcher matcher = HOUR_PATTERN.matcher(text);
        int hours = -1;
        String rawHours = "";
        String indicator = "";
        if (matcher.find()) {
            if (matcher.groupCount() >= 2) {
                rawHours = matcher.group(1);
                indicator = matcher.group(2);
                try {
                    hours = Integer.parseInt(rawHours);
                    if (hours > 0 && hours <= 12) {
                        time.set(Calendar.HOUR, hours);
                        time.set(Calendar.MINUTE, 0);
                        if (indicator.equalsIgnoreCase("am")) {
                            time.set(Calendar.AM_PM, Calendar.AM);
                        } else if (indicator.equalsIgnoreCase("pm")) {
                            time.set(Calendar.AM_PM, Calendar.PM);
                        }
                        return true;
                    }

                } catch (NumberFormatException e) {
                    Log.e(TAG, "Unable to parse hours: " + rawHours, e);
                }
            }
        }

        return false;

    }

    private static boolean setHourOfDay(CharSequence text, Calendar time) {
        Matcher matcher = HOUR_OF_DAY_PATTERN.matcher(text);
        String rawHours = "";
        String rawMinutes = "";
        if (matcher.find()) {
            if (matcher.groupCount() >= 2) {
                rawHours = matcher.group(1);
                rawMinutes = matcher.group(2);
                try {
                    int hours = Integer.parseInt(rawHours);
                    int minutes = Integer.parseInt(rawMinutes);
                    if (hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60) {
                        time.set(Calendar.HOUR_OF_DAY, hours);
                        time.set(Calendar.MINUTE, minutes);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Unable to parse hours: " + rawHours, e);
                }
            }
        }
        return false;
    }

}


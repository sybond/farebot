/*
 * NextfareUtil.java
 *
 * Copyright 2015-2016 Michael Farrell <micolous+git@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.codebutler.farebot.transit.nextfare;

import com.codebutler.farebot.util.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Misc utilities for parsing Nextfare Cards
 * @author Michael Farrell
 */
public final class NextfareUtil {

    /**
     * Date format:
     *
     * 0001111 1100 00100 = 2015-12-04
     * yyyyyyy mmmm ddddd
     *
     * Bottom 11 bits = minutes since 00:00
     * Time is represented in localtime, Australia/Brisbane.
     *
     * Assumes that data has already been byte-reversed for big endian parsing.
     *
     * @param timestamp Four bytes of input representing the timestamp to parse
     * @return Date and time represented by that value
     */
    public static GregorianCalendar unpackDate(byte[] timestamp) {
        int minute = Utils.getBitsFromBuffer(timestamp, 5, 11);
        int year = Utils.getBitsFromBuffer(timestamp, 16, 7) + 2000;
        int month = Utils.getBitsFromBuffer(timestamp, 23, 4);
        int day = Utils.getBitsFromBuffer(timestamp, 27, 5);

        //Log.i(TAG, "unpackDate: " + minute + " minutes, " + year + '-' + month + '-' + day);

        if (minute > 1440) throw new AssertionError("Minute > 1440");
        if (minute < 0) throw new AssertionError("Minute < 0");

        if (day > 31) throw new AssertionError("Day > 31");
        if (month > 12) throw new AssertionError("Month > 12");

        GregorianCalendar d = new GregorianCalendar(year, month-1, day);
        d.add(Calendar.MINUTE, minute);

        return d;
    }
}

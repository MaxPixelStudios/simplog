package cn.maxpixel.simplog.test;

import cn.maxpixel.simplog.Level;

public class LevelTest {
    public void testConstructor() {
        try {
            new Level("TEST", (short) -2);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }

        try {
            new Level("TEST", (short) 10001);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }

        try {
            new Level(null, (short) 5000);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }
}
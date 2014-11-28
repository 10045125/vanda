/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haarman.listviewanimations.swinginadapters.prepared;

import com.haarman.listviewanimations.swinginadapters.SingleAnimationAdapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An implementation of the AnimationAdapter class which applies a
 * swing-in-from-the-right-animation to views.
 */
public class SwingRightInAnimationAdapter extends SingleAnimationAdapter {

    private final long mAnimationDelayMillis;

    private final long mAnimationDurationMillis;

    public SwingRightInAnimationAdapter(BaseAdapter baseAdapter) {
        this(baseAdapter, DEFAULTANIMATIONDELAYMILLIS, DEFAULTANIMATIONDURATIONMILLIS);
    }

    public SwingRightInAnimationAdapter(BaseAdapter baseAdapter, long animationDelayMillis) {
        this(baseAdapter, animationDelayMillis, DEFAULTANIMATIONDURATIONMILLIS);
    }

    public SwingRightInAnimationAdapter(BaseAdapter baseAdapter, long animationDelayMillis,
            long animationDurationMillis) {
        super(baseAdapter);
        mAnimationDelayMillis = animationDelayMillis;
        mAnimationDurationMillis = animationDurationMillis;
    }

    @Override
    protected long getAnimationDelayMillis() {
        return mAnimationDelayMillis;
    }

    @Override
    protected long getAnimationDurationMillis() {
        return mAnimationDurationMillis;
    }

    @Override
    protected Animator getAnimator(ViewGroup parent, View view) {
        return ObjectAnimator.ofFloat(view, "translationX", parent.getWidth(), 0);
    }
}

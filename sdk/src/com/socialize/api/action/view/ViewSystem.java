/*
 * Copyright (c) 2012 Socialize Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.socialize.api.action.view;

import android.location.Location;
import com.socialize.api.SocializeSession;
import com.socialize.entity.Entity;
import com.socialize.listener.view.ViewListener;

/**
 * @author Jason Polites
 */
public interface ViewSystem {

	public static final String ENDPOINT = "/view/";
	
	public void addView(SocializeSession session, Entity entity, Location location, ViewListener listener);
	
	@Deprecated
	public void getView(SocializeSession session, Entity entity, ViewListener listener);
	
	public void getView(SocializeSession session, long id, ViewListener listener);
	
	@Deprecated
	public void getViewsByEntity(SocializeSession session, String entityKey, int startIndex, int endIndex, ViewListener listener);

	@Deprecated
	public void getViewsByUser(SocializeSession session, long userId, int startIndex, int endIndex, ViewListener listener);

}
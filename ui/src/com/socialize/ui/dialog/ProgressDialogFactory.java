/*
 * Copyright (c) 2011 Socialize Inc.
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
package com.socialize.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.socialize.log.SocializeLogger;

/**
 * Safely renders progress dialogs
 * @author Jason Polites
 */
public class ProgressDialogFactory {
	
	private SocializeLogger logger;

	public ProgressDialog show(Context context, String title, String message) {
		try {
			if(context instanceof Activity) {
				Activity activity = (Activity) context;
				while(activity.getParent() != null) {
					activity = activity.getParent();
				}
				context = activity;
			}
			
			ProgressDialog dialog = new SafeProgressDialog(context);
			dialog.setTitle("Posting comment");
			dialog.setMessage("Please wait...");
			dialog.show();
			return dialog;
		}
		catch (Exception e) {
			if(logger != null) {
				logger.error("Error displaying progress dialog", e);
			}
			else {
				e.printStackTrace();
			}
			
			return null;
		}

	}

	public SocializeLogger getLogger() {
		return logger;
	}

	public void setLogger(SocializeLogger logger) {
		this.logger = logger;
	}
	
	
}
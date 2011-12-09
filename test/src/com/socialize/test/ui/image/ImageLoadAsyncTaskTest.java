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
package com.socialize.test.ui.image;

import java.util.Map;
import java.util.Queue;

import android.graphics.Bitmap;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;
import com.socialize.test.ui.SocializeUIActivityTest;
import com.socialize.ui.image.ImageLoadAsyncTask;
import com.socialize.ui.image.ImageLoadRequest;
import com.socialize.ui.image.ImageUrlLoader;
import com.socialize.util.CacheableDrawable;
import com.socialize.util.DrawableCache;
import com.socialize.util.SafeBitmapDrawable;

/**
 * @author Jason Polites
 *
 */
@UsesMocks ({
	ImageUrlLoader.class,
	DrawableCache.class,
	Queue.class,
	Map.class,
	CacheableDrawable.class,
	SafeBitmapDrawable.class,
	ImageLoadRequest.class
})
public class ImageLoadAsyncTaskTest extends SocializeUIActivityTest {


	public void testImageLoadAsyncTaskImageInCache() {
		runTest(true);
	}
	
	public void testImageLoadAsyncTaskImageNotInCache() {
		runTest(false);
	}
	
	@SuppressWarnings("unchecked")
	protected void runTest(boolean imageInCache) {
		// Can't mock, so just create a dummy one
		final Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		
		final Queue<ImageLoadRequest> requests = AndroidMock.createMock(Queue.class);
		final Map<String, ImageLoadRequest> pendingRequests = AndroidMock.createMock(Map.class);
		final ImageLoadRequest request = AndroidMock.createMock(ImageLoadRequest.class);
		final DrawableCache cache = AndroidMock.createMock(DrawableCache.class);
		final CacheableDrawable drawable = AndroidMock.createMock(CacheableDrawable.class, bmp, "foo");
		final SafeBitmapDrawable bitmap = AndroidMock.createMock(SafeBitmapDrawable.class);
		final String url = "foobar";
		
		PublicImageLoadAsyncTask task = new PublicImageLoadAsyncTask() {

			@Override
			public Queue<ImageLoadRequest> makeRequests() {
				return requests;
			}

			@Override
			public Map<String, ImageLoadRequest> makePendingRequests() {
				return pendingRequests;
			}

			@Override
			public void doExecute() {
				// Do nothing for this test, we will call doInBackground manually
				addResult(2, "doExecute_called");
			}

			@Override
			public void doCancel(boolean cancel) {
				// Do nothing for this test
				addResult(3, "doCancel_called");
			}

			@Override
			public SafeBitmapDrawable loadImage(String url) throws Exception {
				addResult(0, url); // to verify we were called.
				return bitmap;
			}

			@Override
			public void doWait() throws InterruptedException {
				addResult(1, "wait_called"); // To verify we were called
				
				// Force stop
				stop();
			}
		};
		
		// Run once only
		AndroidMock.expect(requests.isEmpty()).andReturn(false).once();
		AndroidMock.expect(requests.isEmpty()).andReturn(true).once();
		AndroidMock.expect(requests.poll()).andReturn(request).once();
		AndroidMock.expect(request.isCanceled()).andReturn(false).once();
		AndroidMock.expect(request.getUrl()).andReturn(url).once();
		
		if(imageInCache) {
			AndroidMock.expect(cache.get(url)).andReturn(drawable).once();
		}
		else {
			AndroidMock.expect(cache.get(url)).andReturn(null).once();
		}
		
		
		AndroidMock.expect(pendingRequests.remove(url)).andReturn(request);
		
		if(imageInCache) {
			request.notifyListeners(drawable);
		}
		else {
			request.notifyListeners(bitmap);
		}
		
		requests.clear();
		pendingRequests.clear();
		
		AndroidMock.replay(request);
		AndroidMock.replay(requests);
		AndroidMock.replay(pendingRequests);
		AndroidMock.replay(cache);
		
		task.setCache(cache);
		task.start();
		task.doInBackground((Void[])null);
		
		AndroidMock.verify(request);
		AndroidMock.verify(requests);
		AndroidMock.verify(pendingRequests);
		AndroidMock.verify(cache);
		
		String urlAfter = getResult(0);
		String wait_called = getResult(1);
		
		String doExecute_called = getResult(2);
		String doCancel_called = getResult(3);
		
		if(!imageInCache) assertNotNull(urlAfter);
		assertNotNull(wait_called);
		assertNotNull(doExecute_called);
		assertNotNull(doExecute_called);
		
		if(!imageInCache) assertEquals(url, urlAfter);
		assertEquals("wait_called", wait_called);
		assertEquals("doExecute_called", doExecute_called);
		assertEquals("doCancel_called", doCancel_called);
	}
	
	public class PublicImageLoadAsyncTask extends ImageLoadAsyncTask {
		@Override
		public Void doInBackground(Void... args) {
			return super.doInBackground(args);
		}

		@Override
		public SafeBitmapDrawable loadImage(String url) throws Exception {
			return super.loadImage(url);
		}

		@Override
		public Queue<ImageLoadRequest> makeRequests() {
			return super.makeRequests();
		}

		@Override
		public Map<String, ImageLoadRequest> makePendingRequests() {
			return super.makePendingRequests();
		}

		@Override
		public void doExecute() {
			super.doExecute();
		}

		@Override
		public void doCancel(boolean cancel) {
			super.doCancel(cancel);
		}

		@Override
		public void doWait() throws InterruptedException {
			super.doWait();
		}
	}
}
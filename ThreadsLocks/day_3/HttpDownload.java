import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

interface ProgressListener {
	void onProgress(int current);
}

class Downloader extends Thread {
	private InputStream in;
	private OutputStream out;
	private CopyOnWriteArrayList<ProgressListener> listeners;

	public Downloader(URL url, String outputFilename) throws IOException {
		in = url.openConnection().getInputStream();
		out = new FileOutputStream(outputFilename);
		listeners = new CopyOnWriteArrayList<ProgressListener>();
	}
	public synchronized void addListener(ProgressListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeListener(ProgressListener listener) {
		listeners.remove(listener);
	}
	private synchronized void updateProgress(int n) {
		for(ProgressListener listener : listeners) {
			listener.onProgress(n);
		}
	}

	public void run() {
		int n = 0, total = 0;
		byte[] buffer = new byte[1024];

		try {
			while((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
				total += n;
				updateProgress(total);
			}
			out.flush();
		} catch(IOException e) {}
	}
}

public class HttpDownload {
	public static void main(String[] args) throws Exception {
		// String url = "http://download.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2";
		String url = "http://google.com";
		URL from = new URL(url);
		Downloader downloader = new Downloader(from, "download.out");
		downloader.start();
		downloader.addListener(new ProgressListener() {
				public void onProgress(int n) { System.out.print("\r"+n); System.out.flush(); }
				public void onComplete(boolean success) {}
			});
		downloader.join();
	}
}

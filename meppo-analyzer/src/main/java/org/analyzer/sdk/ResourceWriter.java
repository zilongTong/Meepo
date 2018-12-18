package org.analyzer.sdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ResourceWriter {
	private String _filePath;
	private FileOutputStream _fileOutputStream = null;
	private OutputStreamWriter _outputStreamWriter = null;
	private BufferedWriter _bufferedWriter = null;
	private File _file = null;

	private long count = 0;
	
	public ResourceWriter(String dis, String cs) {
		_filePath = dis;
		init(cs);
	}
	public ResourceWriter(String dis) {
		_filePath = dis;
		init("utf-8");
	}

	public ResourceWriter(String dis, boolean isAppend) {
		_filePath = dis;
		init("utf-8", isAppend);
	}
	public ResourceWriter(File f) {
		if(f == null) {
			throw new NullPointerException("the file f is null.");
		}
		_file = f;
		init("utf-8");
	}
	public ResourceWriter(File f, String cs) {
		_file = f;
		init(cs);
	}
	public void init(String cs) {
		init(cs, false);
	}
	public void init(String cs, boolean isAppend) {
		try {
			if(_file == null) {
				_fileOutputStream = new FileOutputStream(new File(_filePath), isAppend);
			} else {
				_fileOutputStream = new FileOutputStream(_file);	
			}
			_outputStreamWriter = new OutputStreamWriter(_fileOutputStream, cs);
			_bufferedWriter = new BufferedWriter(_outputStreamWriter, 512);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void write(String line) {
		try {
			_bufferedWriter.write(line + "\n");
			if(count ++ > 10000) {
				_bufferedWriter.flush();
				count = 0;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void flush() {
		try {
			_bufferedWriter.flush();
			count = 0;
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void close() {
		try {
			_bufferedWriter.close();
			_outputStreamWriter.close();
			_fileOutputStream.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

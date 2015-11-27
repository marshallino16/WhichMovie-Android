package genyus.com.whichmovie.classes;

public class RequestReturn {

	public String json;
	public int code;
	
	public RequestReturn(String json, int code) {
		this.json = json;
		this.code = code;
	}
}

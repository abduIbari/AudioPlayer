package studiplayer.audio;

public class NotPlayableException extends Exception{
	private static final long serialVersionUID = 1L;
	@Override
	public String toString() {
		return pathname + super.toString();
	}

	private String pathname;
	

	public NotPlayableException(String pathname, String msg){
		super(msg);
		this.setPathname(pathname);
	}
	
	public NotPlayableException(String pathname, Throwable t){
		super(t);
		this.setPathname(pathname);
	}
	
	public NotPlayableException(String pathname, String msg, Throwable t){
		super(msg, t);
		this.setPathname(pathname);
	}

	public String getPathname() {
		return pathname;
	}

	public void setPathname(String pathname) {
		this.pathname = pathname;
	}
}


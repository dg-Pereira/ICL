package compiler;

public class FrameLocation {
	public final int destinyFrame;
	public final int loc;
	public final String type;

	public FrameLocation(int destinyFrame, int loc, String type) {
		this.destinyFrame = destinyFrame;
		this.loc = loc;
		this.type = type;
	}
}

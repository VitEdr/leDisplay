package vit.argon.ledisplay;

public class DataPackage {
	//Define variables
	private byte[][] msg_head = {
			/*msg1*/{0x01, 0x05, 0x02, 0x55, (byte)0xAA, (byte)0x5A, (byte)0xA5, 0x06},
			/*msg2*/{0x02, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A},
			/*msg3*/{0x03, 0x10,  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x14}
			};
	private byte[][] msg_body;
	private byte[][] msg_tail = {
			/*msg5*/{0x05, 0x03, 0x0C, 0x01, 0x01, 0x16},
			/*msg6*/{0x06, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0E},
			/*msg7*/{0x07, 0x02, 0x00, 0x00, 0x09},
			/*msg8*/{0x09, 0x07, 0x20, 0x15, 0x04, 0x30, 0x16, 0x56, 0x17, (byte)0xFC}
			};
	//Constructor
	DataPackage(){
		
	}
	//
	
	public byte controlSum(byte[] m){
		byte sum = 0;
		for (int bt = 0; bt < m.length-1; bt++){
			sum += m[bt];
		}
		return sum;
	}
	
	public void setEffect(int pos, int effect){
		msg_head[1][pos + 1] &= 0xF0;//reset effect
		msg_tail[1][pos + 1] &= 0xF0;//reset effect
		if (effect < 5){
			msg_head[1][pos + 1] |= effect;//set new effect
			msg_tail[1][pos + 1] |= effect;//set new effect
		}
		else if (effect > 5){
			msg_head[1][pos + 1] |= effect + 1;//set new effect
			msg_tail[1][pos + 1] |= effect + 1;//set new effect
		}
		msg_head[1][msg_head[1].length-1] = controlSum(msg_head[1]);
		msg_tail[1][msg_tail[1].length-1] = controlSum(msg_tail[1]);
	}
	
	public void setBrightness(int bright){
		msg_tail[0][3] = (byte) bright;
		msg_tail[0][msg_tail[0].length-1] = controlSum(msg_tail[0]);
	}
	
	public void setSpeed(int pos, int speed){
		msg_head[1][pos + 1] &= 0x0F;//reset speed
		msg_tail[1][pos + 1] &= 0x0F;//reset speed
		msg_head[1][pos + 1] |= (7 - speed)*16;//set speed
		msg_tail[1][pos + 1] |= (7 - speed)*16;//set speed
		msg_head[1][msg_head[1].length-1] = controlSum(msg_head[1]);
		msg_tail[1][msg_tail[1].length-1] = controlSum(msg_tail[1]);
	}
	
	public void setLength(int pos, int length){
		msg_head[2][2*pos + 1] = (byte) length;
		msg_head[2][msg_head[2].length-1] = controlSum(msg_head[2]);
	}
	
	public void setFontSize(int size){
		msg_tail[0][2] = (byte) size;
		msg_tail[1][msg_tail[1].length-1] = controlSum(msg_tail[1]);
	}
	
	public void setBody(byte[] msg, int BODY_LENGTH){
		if(BODY_LENGTH != 0){
			msg_body = new byte[1][BODY_LENGTH];
			for(int i = 0; i < BODY_LENGTH; i++)
				msg_body[0][i] = msg[i];
		}
	}
	
	public byte[][] getHead(){
		return msg_head;
	}
	
	public byte[][] getBody(){
		return msg_body;
	}
	
	public byte[][] getTail(){
		return msg_tail;
	}
}

package in.co.rays.proj4.util;
/**
 * Contains Email Message
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 *
 */
public class EmailMessage {
	// Contains comma separated TO addresses
         private String to = null;
      //Sender address
         private String from = null;
      //contains comma seperated CC address
         private String cc = null;
      //contains comma seperated BCC address
         private String bcc = null;
      //contains msg subject
         private String subject = null;
      //conatains msg
         private String message = null;
         
         //type of msg whether it is Html or text, default is text
         private int messageType = TEXT_MSG;

         public static final int HTML_MSG = 1;
         public static final int TEXT_MSG = 2;
		
         public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		
		public String getCc() {
			return cc;
		}
		public void setCc(String cc) {
			this.cc = cc;
		}
		public String getBcc() {
			return bcc;
		}
		public void setBcc(String bcc) {
			this.bcc = bcc;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getMessageType() {
			return messageType;
		}
		public void setMessageType(int messageType) {
			this.messageType = messageType;
		}
		}
         
         

	



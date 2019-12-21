package org.xjtu.framework.core.util;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class MailUtil {
	// 閭欢鍙戦�佽�呭湴鍧�
    private static final String SenderEmailAddr = "mcgrady900821@163.com";
    // 閭欢鍙戦�佽�呴偖绠辩敤鎴�
    private static final String SMTPUserName = "mcgrady900821";
    
    // 閭欢鍙戦�佽�呴偖绠卞瘑鐮�
    private static final String SMTPPassword = "HUANGhuang0821";
    // 閭欢鍙戦�佽�呴偖绠盨MTP鏈嶅姟鍣�
    private static final String SMTPServerName = "smtp.163.com";
    // 浼犺緭绫诲瀷
    private static final String TransportType = "smtp";
    // 灞炴��
    private static Properties props;
    /**
     * 绉佹湁鏋勯�犲嚱鏁帮紝闃叉澶栫晫鏂板缓鏈疄鐢ㄧ被鐨勫疄渚嬶紝鍥犱负鐩存帴浣跨敤MailUtil.sendMail鍙戦�侀偖浠跺嵆鍙�
     *
     */
    private MailUtil() {
    }
    /**
     * 闈欐�佹瀯閫犲櫒
     */
    static {
        MailUtil.props = new Properties();
        // 瀛樺偍鍙戦�侀偖浠舵湇鍔″櫒鐨勪俊鎭�
        MailUtil.props.put("mail.smtp.host", MailUtil.SMTPServerName);
        // 鍚屾椂閫氳繃楠岃瘉
        MailUtil.props.put("mail.smtp.auth", "true");
    }
    /**
     * 鍙戦�侀偖浠�
     * @param emailAddr:鏀朵俊浜洪偖浠跺湴鍧�
     * @param mailTitle:閭欢鏍囬
     * @param mailConcept:閭欢鍐呭
     */
    public static boolean sendMail(String emailAddr, String mailTitle,
            String mailConcept,String contentType,String attachmentPath,String attachmentName) {
        // 鏍规嵁灞炴�ф柊寤轰竴涓偖浠朵細璇濓紝null鍙傛暟鏄竴绉岮uthenticator(楠岃瘉绋嬪簭) 瀵硅薄
        Session s = Session.getInstance(MailUtil.props, null);
        // 璁剧疆璋冭瘯鏍囧織,瑕佹煡鐪嬬粡杩囬偖浠舵湇鍔″櫒閭欢鍛戒护锛屽彲浠ョ敤璇ユ柟娉�
        s.setDebug(false);
        
        // 鐢遍偖浠朵細璇濇柊寤轰竴涓秷鎭璞�
        Message message = new MimeMessage(s);
        try {
            // 璁剧疆鍙戜欢浜�
            Address from = new InternetAddress(MailUtil.SenderEmailAddr);
            message.setFrom(from);
            // 璁剧疆鏀朵欢浜�
            Address to = new InternetAddress(emailAddr);
            message.setRecipient(Message.RecipientType.TO, to);
            // 璁剧疆涓婚
            message.setSubject(mailTitle);
            // 璁剧疆淇′欢鍐呭
            message.setText(mailConcept);
            if(contentType!=null){
            	message.setContent(mailConcept, contentType);
            }
            // 璁剧疆鍙戜俊鏃堕棿
            message.setSentDate(new Date());
            //璁剧疆闄勪欢
            if(attachmentPath!=null&&attachmentName!=null){
                DataSource source = new FileDataSource(attachmentPath);
                message.setDataHandler(new DataHandler(source));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                message.setFileName("=?GBK?B?"+enc.encode(attachmentName.getBytes())+"?=");
            }
            // 瀛樺偍閭欢淇℃伅
            message.saveChanges();
            
            Transport transport = s.getTransport(MailUtil.TransportType);
            // 瑕佸～鍏ヤ綘鐨勭敤鎴峰悕鍜屽瘑鐮侊紱
            transport.connect(MailUtil.SMTPServerName, MailUtil.SMTPUserName,
                    MailUtil.SMTPPassword);
            // 鍙戦�侀偖浠�,鍏朵腑绗簩涓弬鏁版槸鎵�鏈夊凡璁惧ソ鐨勬敹浠朵汉鍦板潃
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("发送邮件，邮件地址:" + emailAddr + " 标题:" + mailTitle
                    + " 内容:" + mailConcept + "成功!");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("发送邮件，邮件地址:" + emailAddr + " 标题:" + mailTitle
                    + " 内容:" + mailConcept + "失败原因是" + e.getMessage());
            return false;
        }
    }
}

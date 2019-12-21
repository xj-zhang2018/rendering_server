package cn.twinkling.stream.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.xjtu.framework.core.base.model.User;

import com.opensymphony.xwork2.ActionContext;

import cn.twinkling.stream.config.Configurations;
import cn.twinkling.stream.servlet.FormDataServlet;
import cn.twinkling.stream.servlet.Range;
import cn.twinkling.stream.servlet.StreamServlet;

import static org.xjtu.framework.modules.manager.action.UserManageAction.existedUser;

/**
 * IO--closing, getting file name ... main function method
 */
public class IoUtil {
    static final Pattern RANGE_PATTERN = Pattern.compile("bytes \\d+-\\d+/\\d+");

    /**
     * According the key, generate a file (if not exist, then create
     * a new file).
     *
     * @param filename
     * @return
     * @throws IOException
     */
    /* ActionContext actionContext=null;
    Map session =null;
    User    existedUser=null;
    String UserName=null;
    static{
    	
    	 ActionContext actionContext = ActionContext.getContext();
         Map session = actionContext.getSession();
         User    existedUser=(User) session.get("existedUser");
         String UserName=existedUser.getName();
    	
    	
    }*/
    
   
    public static File getFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty())
            return null;
        String name = filename.replaceAll("/", Matcher.quoteReplacement(File.separator));

        String config_filepath=Configurations.getFileRepository();
        String username=existedUser.getName();
        String now_filepath=config_filepath+"/"+username;

       System.out.println("配置文件修改后的上传文件路径1："+now_filepath);

        File f = new File(now_filepath+ File.separator + name);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();

        return f;
    }

    /**
     * Acquired the file.
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static File getTokenedFile(String key) throws IOException {
        if (key == null || key.isEmpty())
            return null;
        String config_filepath=Configurations.getFileRepository();
        String username=existedUser.getName();
        String now_filepath=config_filepath+"/"+username;

        System.out.println("配置文件修改后的上传文件路径2："+now_filepath);
        
        File f = new File(now_filepath+ File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();

        return f;
    }

    public static void storeToken(String key) throws IOException {
        if (key == null || key.isEmpty())
            return;

        String config_filepath=Configurations.getFileRepository();
        String username=existedUser.getName();
        String now_filepath=config_filepath+"/"+username;

        System.out.println("配置文件修改后的上传文件路径3："+now_filepath);
        
        
        File f = new File(now_filepath+ File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();
        
        
        //hghgjgjgjhgjhgj
    }

    /**
     * close the IO stream.
     *
     * @param stream
     */
    public static void close(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {
        }
    }

    /**
     * 获取Range参数
     *
     * @param req
     * @return
     * @throws IOException
     */
    public static Range parseRange(HttpServletRequest req) throws IOException {
        String range = req.getHeader(StreamServlet.CONTENT_RANGE_HEADER);
        Matcher m = RANGE_PATTERN.matcher(range);
        if (m.find()) {
            range = m.group().replace("bytes ", "");
            String[] rangeSize = range.split("/");
            String[] fromTo = rangeSize[0].split("-");

            long from = Long.parseLong(fromTo[0]);
            long to = Long.parseLong(fromTo[1]);
            long size = Long.parseLong(rangeSize[1]);

            return new Range(from, to, size);
        }
        throw new IOException("Illegal Access!");
    }

    /**
     * From the InputStream, write its data to the given file.
     */
    public static long streaming(InputStream in, String key, String fileName) throws IOException {
        OutputStream out = null;
        File f = getTokenedFile(key);
        try {
            out = new FileOutputStream(f);

            int read = 0;
            final byte[] bytes = new byte[FormDataServlet.BUFFER_LENGTH];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } finally {
            close(out);
        }
        /** rename the file * fix the `renameTo` bug */
        File dst = IoUtil.getFile(fileName);
        dst.delete();
        f.renameTo(dst);

        long length = getFile(fileName).length();
        /** if `STREAM_DELETE_FINISH`, then delete it. */
        if (Configurations.isDeleteFinished()) {
            dst.delete();
        }

        return length;
    }
}

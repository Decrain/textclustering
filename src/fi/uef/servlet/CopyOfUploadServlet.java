package fi.uef.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * Servlet implementation class for Servlet: UploadServlet
 * 
 */
public class CopyOfUploadServlet extends javax.servlet.http.HttpServlet implements
        javax.servlet.Servlet {
    File tmpDir = null;// ��ʼ���ϴ��ļ�����ʱ���Ŀ¼
    File saveDir = null;// ��ʼ���ϴ��ļ���ı���Ŀ¼

    public CopyOfUploadServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
			// �ϴ���������
			response.setContentType("text/html;charset=gb2312");
			request.setCharacterEncoding("UTF-8");

			// ����Ŀ¼
			// ָ���ϴ��ļ�����ʱĿ¼, ����Ŀ��upload_tmp_dir��,
			//C:\tomcat6\webapps/upload/upload_tmp_dir  ��󲻴�\
			String realPath = request.getSession().getServletContext()
					.getRealPath(File.separator);
			String up_tempDir = realPath + "upload_tmp_dir";
			response.getWriter().println("upload_temp directory is: "+up_tempDir + "<br>");
			tmpDir = new File(up_tempDir);
			if (!tmpDir.isDirectory())
				tmpDir.mkdir();

			// ָ���ϴ���Ŀ¼,����Ŀ��upload_tmp_dir��, C:\tomcat6\webapps/upload/upload_dir ,��󲻴�\
			String upload_Dir = realPath + "upload_dir";
			response.getWriter().println("upload directory is: "+upload_Dir + "<br>");
			saveDir = new File(upload_Dir);
			if (!saveDir.isDirectory())
				saveDir.mkdir();
			
            if (ServletFileUpload.isMultipartContent(request)) {
                DiskFileItemFactory dff = new DiskFileItemFactory();// �����ö���
                dff.setRepository(tmpDir);// ָ���ϴ��ļ�����ʱĿ¼
                dff.setSizeThreshold(1024000);// ָ�����ڴ��л������ݴ�С,��λΪbyte
                ServletFileUpload sfu = new ServletFileUpload(dff);// �����ö���
                sfu.setFileSizeMax(5000000);// ָ�������ϴ��ļ������ߴ�
                sfu.setSizeMax(10000000);// ָ��һ���ϴ�����ļ����ܳߴ�
                FileItemIterator fii = sfu.getItemIterator(request);// ����request
                                                                    // ����,������FileItemIterator����
                while (fii.hasNext()) {
    				FileItemStream fis = fii.next();// �Ӽ����л��һ���ļ���
    				if (!fis.isFormField() && fis.getName().length() > 0) {// ���˵����з��ļ���
    				
    					String fileName=fis.getName().substring(
    							fis.getName().lastIndexOf(File.separator) + 1);
    					// D:\hao\����\contact_list.txt ������·�� fis.getName()
    					response.getWriter().println("full path of upload file:"+fis.getName() + "<br>");
    					//contact_list.txt  ֻ���ļ���
    					response.getWriter().println("upload file name is: "+fis.getName().substring(fis.getName().lastIndexOf(File.separator) + 1)+ "<br>");

    					BufferedInputStream in = new BufferedInputStream(fis
    							.openStream());// ����ļ�������
    					BufferedOutputStream out = new BufferedOutputStream(
    							new FileOutputStream(new File(saveDir + File.separator
    									+ fileName)));// ����ļ������
    					Streams.copy(in, out, true);// ��ʼ���ļ�д����ָ�����ϴ��ļ���
    				}
    			}

                response.getWriter().println("File upload successfully!!!");// ���ڳɹ���,����������ϴ��ļ��п���,��Ҫ�Ķ�������������
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}




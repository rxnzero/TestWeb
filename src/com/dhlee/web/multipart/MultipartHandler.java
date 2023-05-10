package com.dhlee.web.multipart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MultipartHandler extends HttpServlet {
	private static final String JSON_CONTENT_TYPE = "application/json";
	private static final String JSON_FIELD_NAME = "json-body";
	private static final String FILE_GROUP_NAME = "image-file";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if the request is a multipart request
		if (!ServletFileUpload.isMultipartContent(request)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request is not a multipart request");
			return;
		}

		String jsonString = null;
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Set the maximum size of the files to be uploaded
		factory.setSizeThreshold(1024 * 1024);

		// Set the temporary directory to store uploaded files
		File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(tempDir);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		Map<String, String> fileMap = new HashMap<>();
		InputStream fin = null;
		try {
			byte[] buffer = new byte[1024];
			int read = 0;
			// Parse the multipart request
			List<FileItem> items = upload.parseRequest(request);
			// Process each item in the request
			for (FileItem item : items) {
				if (!item.isFormField()) {
					// This item is a file
					String fieldName = item.getFieldName();
					String fileName = item.getName();
//	                    File file = new File(getServletContext().getRealPath("/") + fileName);
//	                    item.write(file);
					// Handle the uploaded file
					fin = item.getInputStream();
					ByteArrayOutputStream fo = new ByteArrayOutputStream();
					while ((read = fin.read(buffer)) > 0) {
						fo.write(buffer, 0, read);
					}
					int fileSize = fo.size();
					byte[] fileBytes = fo.toByteArray();
					String fileContents = new String(fileBytes);
					System.out.println("[FILE]-------------------------------------------------->");
					System.out.println("Field name = " + fieldName);
					System.out.println("File name = " + fileName + " contents length = " + fileSize);
					System.out.println("File Contents [" + fileContents + "]");
					System.out.println("[FILE]<--------------------------------------------------");
					fileMap.put(fileName, fileContents);
					fin.close();					
				} else {
					// This item is a regular form field
					String fieldName = item.getFieldName();
					String fieldValue = item.getString();
					// Handle the regular form field
					System.out.println("[FIELD] " + fieldName + " [" + fieldValue + "]");
					if (JSON_CONTENT_TYPE.equalsIgnoreCase(item.getContentType()) || JSON_FIELD_NAME.equals(fieldName)) {
						jsonString = fieldValue;
					}
				}
			}
			System.out.println("jsonString => " + jsonString);
			if (jsonString == null) {
				jsonString = "{}";
			} else {
				// parsing json & add file contents
				JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
				if (jsonObject == null) {
					jsonString = "{}";
				} else {
					
					if(fileMap.size() > 1) {
						JSONArray files = new JSONArray();
						for(Map.Entry<String, String> entry: fileMap.entrySet()) {
							JSONObject file = new JSONObject();
							file.put("fileName", entry.getKey());
							file.put("fileContents", entry.getValue());
							files.add(file);
						}
						jsonObject.put(FILE_GROUP_NAME, files);
					}
					else {
						JSONObject fileGroup = new JSONObject();
						for(Map.Entry<String, String> entry: fileMap.entrySet()) {
							fileGroup.put("fileName", entry.getKey());
							fileGroup.put("fileContents", entry.getValue());
						}
						jsonObject.put(FILE_GROUP_NAME, fileGroup);
						jsonString = jsonObject.toJSONString();
					}
					
					
					jsonString = jsonObject.toJSONString();
				}
			}
			// Send a success response
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonString);
		} catch (Exception e) {
			// Handle errors
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Failed to process request: " + e.getMessage());
		}
		finally {
			if(fin != null) {
				try { fin.close(); } catch(Exception ex) { 
						// empty
				}
			}
		}
	}
}

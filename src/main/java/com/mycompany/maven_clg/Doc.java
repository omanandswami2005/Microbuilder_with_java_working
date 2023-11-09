package com.mycompany.maven_clg;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Doc extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/msword"); // Set content type to MS Word
        response.setHeader("Content-Disposition", "attachment; filename=output.docx"); // Force download

        // Load the Word document, manipulate it, and write its content to the response
        String course = request.getParameter("course");
        String co = request.getParameter("COI");
        String name = request.getParameter("name");
        String mptitle = request.getParameter("microprojectTitle");
        String cosubject = request.getParameter("courseSubject");
        String teacher = request.getParameter("teacherName");
        String studentenr = request.getParameter("studentEnrollment");

        // Load the Word document into memory
        try (InputStream inputStream = getServletContext().getResourceAsStream("/input.docx")) {
            XWPFDocument document = new XWPFDocument(inputStream);

            // Define the placeholders and corresponding values
            Map<String, String> placeholderMap = new HashMap<>();
            placeholderMap.put("{course}", course);
            placeholderMap.put("{COI}", co);
            placeholderMap.put("{MICROPROJECT_TITLE}", mptitle);
            placeholderMap.put("{COSUBJECT}", cosubject);
            placeholderMap.put("{TEACHER_NAME}", teacher);
            placeholderMap.put("{STUDENT_ENR}", studentenr);
            placeholderMap.put("{STUDENT_NAME}", name);

            // Replace the placeholders in the document
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null) {
                        for (Map.Entry<String, String> entry : placeholderMap.entrySet()) {
                            if (text.contains(entry.getKey())) {
                                text = text.replace(entry.getKey(), entry.getValue());
                                run.setText(text, 0);
                            }
                        }
                    }
                }
            }

            // Write the modified document to the response
            document.write(response.getOutputStream());
            document.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
        // Handle POST request if needed
    }

    @Override
    public String getServletInfo() {
        return "About Servlet";
    }
}
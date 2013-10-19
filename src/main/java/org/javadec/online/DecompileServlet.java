package org.javadec.online;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.javadec.ClassAstBuilder;
import org.javadec.Disassembler;
import org.javadec.asm.tree.ClassNode;
import org.javadec.ast.Clazz;
import org.javadec.codegen.CodeGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class DecompileServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(DecompileServlet.class.getName());

    private final ClassAstBuilder astBuilder = new ClassAstBuilder();
    private final CodeGenerator codeGenerator = new CodeGenerator();
    private final Disassembler disassembler = new Disassembler();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            FileItemIterator iterator = new ServletFileUpload().getItemIterator(request);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();

                if (!item.isFormField() && "file".equals(item.getFieldName())) {
                    log.warning("Got an uploaded file: " + item.getName());

                    if (!item.getName().endsWith(".class")) throw new RuntimeException("Not a .class file");

                    InputStream stream = new BufferedInputStream(item.openStream());
                    ClassNode classNode = disassembler.disassemble(stream);
                    Clazz clazz = astBuilder.buildAst(classNode);
                    String javaCode = codeGenerator.toJava(clazz);

                    String fileName = clazz.getName() + ".java";
                    response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                    response.setContentType("application/octet-stream");
                    response.getWriter().write(javaCode);
                }
            }
        } catch (Exception ex) {
            log.warning("Decompilation failed: " + ex.getMessage());
            throw new ServletException(ex);
        }
    }

}

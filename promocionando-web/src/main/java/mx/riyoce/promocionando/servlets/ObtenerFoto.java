/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.servlets;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.riyoce.promocionando.entities.ImagenProducto;
import mx.riyoce.promocionando.sessions.ProductoSessionBean;

/**
 *
 * @author Ricardo Ceron
 */
@WebServlet(name = "ObtenerFoto", urlPatterns = {"/ObtenerFoto"})
public class ObtenerFoto extends HttpServlet {

    @EJB
    private ProductoSessionBean psb;

    byte[] png = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A};
    byte[] jpg = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    byte[] gif = new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38};

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        String etag = request.getHeader("If-None-Match");
        String tag;
        long mid = 0;
        try {
            mid = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            System.out.println("Error al obtener el id del suario(servlet): " + e.getMessage());
        }
        byte[] f = null;
        String filename = "image-not-found.png";

        ImagenProducto foto = psb.getImagenProductoById(mid);

        if (foto == null) {
            String path = request.getServletContext().getRealPath("/resources/images/" + filename);
            try {
                tag = "silueta";
                filename = "image-not-found.png";
                tag = "camafeom";
                path = request.getServletContext().getRealPath("/resources/images/" + filename);

            } catch (Exception e) {
                throw new ServletException("No es posible abrir el camafeo.");
            }

            File file = new File(path);
            FileInputStream fin = null;
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int) file.length()];
            fin.read(fileContent);
            f = fileContent;
        } else {
            f = foto.getArchivo();
            tag = String.valueOf(foto.getArchivo().length);
        }
        if (f != null) {

            String mime = "image/xyz";
            if (Arrays.equals(Arrays.copyOfRange(f, 0, 8), png)) {
                mime = "image/png";
            }
            if (Arrays.equals(Arrays.copyOfRange(f, 0, 3), jpg)) {
                mime = "image/jpeg";
            }
            if (Arrays.equals(Arrays.copyOfRange(f, 0, 4), gif)) {
                mime = "image/gif";
            }
            response.setContentType(mime);
            response.setContentLength(f.length);

            if (tag.equals(etag)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
            response.addHeader("Cache-Control", "public");
            response.addHeader("Etag", tag);

            ByteArrayInputStream input = new ByteArrayInputStream(f);
            BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
            try {
                int b;
                byte[] buffer = new byte[500240]; // 500kb buffer
                while ((b = input.read(buffer, 0, 10240)) != -1) {
                    output.write(buffer, 0, b);
                }
            } finally {
                output.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ObtenerFoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ObtenerFoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

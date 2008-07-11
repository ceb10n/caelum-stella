package br.com.caelum.stella.boleto.transformer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;

import javax.imageio.ImageIO;

import br.com.caelum.stella.boleto.GeracaoBoletoException;

import com.lowagie.text.pdf.BaseFont;

/**
 * Writer que sabe escrever num PNG
 * 
 * @see <a
 *      href="http://stella.caelum.com.br/boleto-setup.html">http://stella.caelum.com.br/boleto-setup.html</a>
 * 
 * @author Cauê Guerra
 * @author Paulo Silveira
 * 
 */
public class PNGBoletoWriter implements BoletoWriter {

    private static final int NORMAL_SIZE = 36;
    private static final int BIG_SIZE = 45;

    private final Font fonteSimples;
    private final Font fonteBold;

    private final BufferedImage PNGimage;
    private InputStream stream;
    private final Graphics2D graphics;

    public PNGBoletoWriter() {
        this(2144f, 1900);
    }

    public PNGBoletoWriter(double w, double h) {

        this.PNGimage = new BufferedImage((int) w, (int) h,
                BufferedImage.TYPE_INT_RGB);
        this.graphics = this.PNGimage.createGraphics();

        this.graphics.setColor(Color.white);
        this.graphics.fillRect(0, 0, (int) w, (int) h);
        this.graphics.drawImage(this.PNGimage, 0, 0, null);

        this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.graphics.setColor(Color.BLACK);

        this.fonteBold = new Font(BaseFont.HELVETICA_BOLD, Font.BOLD, BIG_SIZE);

        this.fonteSimples = new Font(BaseFont.HELVETICA, Font.PLAIN,
                NORMAL_SIZE);
    }

    public InputStream toInputStream() {
        if (this.stream == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(this.PNGimage, "PNG", baos);
            } catch (IOException e) {
                throw new GeracaoBoletoException(e); // nao esperado
            }
            this.stream = new ByteArrayInputStream(baos.toByteArray());
        }
        return this.stream;
    }

    public void write(float x, float y, String text) {
        checkIfDocIsClosed();
        this.graphics.setFont(this.fonteSimples);
        this.graphics.drawString(text, scaleX(x), scaleY(y));
    }

    public void writeBold(float x, float y, String text) {
        checkIfDocIsClosed();
        this.graphics.setFont(this.fonteBold);
        this.graphics.drawString(text, scaleX(x), scaleY(y));
    }

    public void writeImage(float x, float y, BufferedImage image, float width,
            float height) throws IOException {

        checkIfDocIsClosed();

        this.graphics.drawImage(image, (int) x, (int) (this.PNGimage
                .getHeight()
                - (height * 4.16f) - (y * 4.16f)), (int) (width * 4.16f),
                (int) (height * 4.16f), null);
    }

    private void checkIfDocIsClosed() {
        if (this.stream != null)
            throw new IllegalStateException(
                    "boleto ja gerado, voce nao pode mais escrever na imagem");
    }

    /*
     * Convertendo coordenadas PDF para PNG
     */
    private float scaleX(float x) {
        return x * 4.16f;
    }

    private float scaleY(float y) {
        y = this.PNGimage.getHeight() - y;
        return y * 4.16f - 6005;
    }

    public boolean newPage() {
        throw new IllegalStateException("Nao é possivel criar uma nova pagina em um arquivo png.");
    }
}

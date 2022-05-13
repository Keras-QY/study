package com.kadima.imageMerage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * 合成图片+二维码+文字
 */
public class ImageUtil {
    /**
     * 定义白色背景图的宽度
     */
    private static final int WIDTH_BACKGROUND = 900;
    /**
     * 定义白色背景图的高度
     */
    private static final int HEIGHT_BACKGROUND = 1300;
    /**
     * 商品在背景图中的宽度
     */
    private static final int WIDTH_PRODUCT = 500;
    /**
     * 商品在背景图中的高度
     */
    private static final int HEIGHT_PRODUCT = 800;
    /**
     * 定义二维码所在的X坐标
     */
    private static final int X_QR_CODE = 600;
    /**
     * 定义二维码所在的Y坐标
     */
    private static final int Y_QR_CODE = 1000;
    /**
     * 二维码在背景图中的宽度
     */
    private static final int WIDTH_QR_CODE = 250;
    /**
     * 二维码在背景图中的高度
     */
    private static final int HEIGHT_QR_CODE = 400;

    /**
     * 缩放图片
     * @param srcImg 目标图片
     * @param width 期望宽
     * @param height 期望高
     * @param isEqualScale 是否等比例缩放
     */
    private static BufferedImage reSize(File srcImg,int width,int height,boolean isEqualScale)  {

        String type = getImageType(srcImg);
        if (type == null) {
            return null;
        }

        if (width < 0 || height < 0) {
            return null;
        }

        BufferedImage srcImage = null;

        try {
            srcImage = ImageIO.read(srcImg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage target = null;
        if (srcImage != null) {
            double sx = (double) width / srcImage.getWidth();
            double sy = (double) height / srcImage.getHeight();
            // 等比例缩放
            if (isEqualScale) {
                if (sx > sy) {
                    sx = sy;
                    width = (int) (sx * srcImage.getWidth());
                } else {
                    sy = sx;
                    height = (int) (sy * srcImage.getHeight());
                }
            }
            //获取原图的颜色数据
            ColorModel cm = srcImage.getColorModel();
            //获取原图的像素数据
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();

            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
            Graphics2D g = target.createGraphics();
            // 设置着色微调
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawRenderedImage(srcImage, AffineTransform.getScaleInstance(sx, sy));
            g.dispose();
        }
        return target;
    }

    /**
     * 获取文件类型
     */
    private static String getImageType(File file){
        if (file != null ){
            String fileName = file.getName();
            int index = fileName.lastIndexOf(".");
            if (index != -1 && index < fileName.length()-1){
                return fileName.substring(index+1);
            }
        }
        return null;
    }


    /**
     * 合成图片获取BufferedImage
     * @param productImage 商品图
     * @param QR_CodeImage 二维码图
     * @param object 文本内容
     * @return
     */
    public static final BufferedImage combineCodeAndProductToBufferedImage(File productImage,File QR_CodeImage,Object object) {

        //将黑色背景图填充为白色
        BufferedImage background = new BufferedImage(WIDTH_BACKGROUND, HEIGHT_BACKGROUND, BufferedImage.TYPE_INT_RGB);
        try {
            for(int x=0;x<WIDTH_BACKGROUND;++x) {
                for (int y = 0; y < HEIGHT_BACKGROUND; ++y) {
                    background.setRGB(x, y, 0x00ffffff);
                }
            }
            //获取画笔
            Graphics2D g = background.createGraphics();
            //图片等比例缩放
            BufferedImage product = reSize(productImage, WIDTH_PRODUCT, HEIGHT_PRODUCT, true);
            BufferedImage code = reSize(QR_CodeImage, WIDTH_QR_CODE, HEIGHT_QR_CODE, true);
            //将二维码和商品画在背景图上
            g.drawImage(product, background.getWidth()/2 - product.getWidth()/2, 0, product.getWidth(), product.getHeight(), null);
            g.drawImage(code, X_QR_CODE, Y_QR_CODE, code.getWidth(), code.getHeight(), null);
            //二维码字体
            draw(g,new Font("TimesRoman",Font.PLAIN,30),Color.BLACK,"长按二维码查看详情",X_QR_CODE,Y_QR_CODE + code.getHeight() + 20);
            //LOT
            draw(g,new Font("楷体",Font.BOLD,40),Color.black,"LOT 1521 描述性文字描述性文字",50,900);
            //起拍价
            draw(g,new Font("圆体",Font.PLAIN,25),Color.red,"起拍价 RMB 100000",50,960);
            //写目录名
            draw(g,new Font("宋体",Font.PLAIN,30),Color.black,"钱币|银元",50,1020);
            //开拍时间
            draw(g,new Font("圆体",Font.PLAIN,30),Color.black,"2022年05月12日 20:00 开拍",50,1070);
            //结束绘画
            g.dispose();
            return background;
        } catch (Exception e) {
            throw new RuntimeException("图片合成失败");
        }
    }

    /**
     * 文字描述
     * @param g 画笔
     * @param font 字体
     * @param color 颜色
     * @param content 内容
     * @param x x坐标
     * @param y y坐标
     */
    private static void draw(Graphics2D g,Font font,Color color,String content,int x,int y){
        if (content == null || content.length() ==0 ){
            return;
        }
        if (x < 0 || y < 0){
            return;
        }
        g.setFont(font);
        g.setColor(color);
        g.drawString(content,x,y);
    }

    /**
     * 合成图片下载到指定的位置
     * @param productImage 商品图
     * @param QR_CodeImage 二维码图
     * @param filePath 合成图片下载路径
     * @param object 文本内容
     */
    public static void combineCodeAndProductToFile(File productImage,File QR_CodeImage,String filePath,Object object)  {

        if (productImage == null || !productImage.exists() || !productImage.isFile())
             new RuntimeException("not found File");
        if (QR_CodeImage == null || !QR_CodeImage.exists() || !QR_CodeImage.isFile())
            throw new RuntimeException("not found File");
        if (filePath == null || filePath.length() ==0){
            throw new RuntimeException("文件下载地址为空");
        }
        FileOutputStream fileOutputStream = null;
        BufferedImage bufferedImage = combineCodeAndProductToBufferedImage(productImage, QR_CodeImage, object);

        try {
            fileOutputStream = new FileOutputStream(filePath);
            ImageIO.write(bufferedImage, "jpg", new FileOutputStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage()+"图片合成失败");
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage()+"输出流关闭失败");
                }
            }
        }

    }
    /**
     * 合成图片转化为base64编码
     * @param productImage 商品图
     * @param QR_CodeImage 二维码图
     * @param object 文本内容
     * @return
     */
    public static final String combineCodeAndProductToBase64(File productImage,File QR_CodeImage,Object object) {
        if (productImage == null || !productImage.exists() || !productImage.isFile())
            new RuntimeException("not found File");
        if (QR_CodeImage == null || !QR_CodeImage.exists() || !QR_CodeImage.isFile())
            throw new RuntimeException("not found File");
        BufferedImage bufferedImage = combineCodeAndProductToBufferedImage(productImage, QR_CodeImage, object);
        InputStream in = null;
        ByteArrayOutputStream bs = null;
        String base64 = null;
        //字节数组流
        try {
        bs = new ByteArrayOutputStream();
        //将合成好的背景图输入到字节数组流中
        ImageIO.write(bufferedImage, "jpg", bs);
        bs.flush();
        bs.close();
        //转换为base64数据类型
        base64 = Base64.getEncoder().encodeToString(bs.toByteArray());
        //return base64;
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bs != null){
                    try {
                        bs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return base64;
        }
    }

    public static void main(String[] args) {
        File file = new File("H:/image/product.png");
        File file2 = new File("H:/image/code.png");
        combineCodeAndProductToFile(file, file2, "H:/image/111.jpg",null);
        String s = combineCodeAndProductToBase64(file, file2, "");
        System.out.println(s);

    }
    }


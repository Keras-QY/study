package com.kadima.imageMerage;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/12
 * @title：
 */

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.core.io.ClassPathResource;

/**
 * @Author 萧何
 * @Date 2020/8/16 17:47
 */
public class ImgUtil {
    /**
     * 定义背景图片的宽度
     */
    private static final int WIDTH_BACKGROUND = 900;
    /**
     * 定义背景图片的高度
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
     * @param destImg 期望位置
     * @param width 期望宽
     * @param height 期望高
     * @param isEqualScale 是否等比例缩放
     */
    public static BufferedImage reSize(File srcImg,int width,int height,boolean isEqualScale)  {
        System.out.println("文件开始缩放");

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
            // smoother than exlax:
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


    // 二维码和图片的合成
    public static void combineCodeAndPicToFile(File productImage,File QR_CodeImage,String content) {

        System.out.println("开始合成：" + System.currentTimeMillis());
        try {
            //将黑色背景图填充为白色
            BufferedImage background = new BufferedImage(WIDTH_BACKGROUND, HEIGHT_BACKGROUND, BufferedImage.TYPE_INT_RGB);
            for(int x=0;x<WIDTH_BACKGROUND;++x) {
                for (int y = 0; y < HEIGHT_BACKGROUND; ++y) {
                    background.setRGB(x, y, 0x00ffffff);
                }
            }
            //获取画笔
            Graphics2D g = background.createGraphics();

            //二维码或小图在大图的左上角坐标
            /*int x = big.getWidth() - small.getWidth()-45; //加是向右，减是向左
            int y = (big.getHeight() - small.getHeight()-50); //加是向下，减是向上*/
            //图片等比例缩放
            BufferedImage big = reSize(productImage, WIDTH_PRODUCT, HEIGHT_PRODUCT, true);
            BufferedImage small = reSize(QR_CodeImage, WIDTH_QR_CODE, HEIGHT_QR_CODE, true);
            //将二维码和商品画在背景图上
            g.drawImage(big, background.getWidth()/2 - big.getWidth()/2, 0, big.getWidth(), big.getHeight(), null);
            g.drawImage(small, X_QR_CODE, Y_QR_CODE, small.getWidth(), small.getHeight(), null);
            //设置二维码字体
            Font font = new Font("宋体", Font.PLAIN, 30);
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString("长按二维码查看详情",X_QR_CODE,Y_QR_CODE+small.getHeight()+20);
            g.setFont(new Font("楷体",Font.BOLD,50));
            g.setColor(Color.red);
            g.drawString(content,50,background.getHeight()/2+100);
            //结束绘画
            g.dispose();
            //为了保证大图背景不变色，formatName必须为"png"
            ImageIO.write(background, "png", new FileOutputStream("H:/image/111.jpg"));
            System.out.println("结束合成" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //合成base64响应前台图片数据展示
    /*public static final String combineCodeAndPicToBase64(MultipartFile file, HttpServletResponse response) {
        ImageOutputStream imOut = null;
        try {
            BufferedImage big = ImageIO.read(file.getInputStream());
            // BufferedImage small = ImageIO.read(new File(fillPicPath));
            //创建二维码
            BufferedImage small = ImgUtil.execute();
            //获取大图的画笔
            Graphics2D g = big.createGraphics();

            //左下角位置
           *//* int x = big.getWidth() - small.getWidth()-45; //加是向右，减是向左
            int y = (big.getHeight() - small.getHeight()-50); //加是向下，减是向上*//*

            //右下角位置
           *//* int x = 45; //加是向右，减是向左
            int y = (big.getHeight() - small.getHeight()-50); //加是向下，减是向上*//*
            //居中位置
            int x = (big.getWidth() - small.getWidth())/2; //加是向右，减是向左
            int y = (big.getHeight() - small.getHeight()-50); //加是向下，减是向上

            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            g.dispose();
            //为了保证大图背景不变色，formatName必须为"png"
            //字节数组流
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            //将图片流转换为字节数组流
            imOut = ImageIO.createImageOutputStream(bs);
            //将合成好的背景图输入到字节数组流中
            ImageIO.write(big, "png", imOut);
            //将字节数组流转换为二进制流
            InputStream in = new ByteArrayInputStream(bs.toByteArray());

            byte[] bytes = new byte[in.available()];
            //读取数组流中的数据
            in.read(bytes);
            //转换为base64数据类型
            String base64 = Base64.getEncoder().encodeToString(bytes);
            System.out.println(base64);

            return "data:image/jpeg;base64," + base64;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static void main(String[] args) throws Exception{
        File file = new File("H:/image/product.png");
        File file2 = new File("H:/image/code.png");
        combineCodeAndPicToFile(file,file2,"hello world \n 1111111111111111111111111111121");

    }
    }


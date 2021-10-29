package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;


public class ImageTesterUnPointNoir {


    public static void main(String args[]) throws Exception {
    	//Je récupère le fichier
        File file = new File("C:/dev/WorkspaceFormationJava/OrganismCounter/src/test/unPointNoir.jpg");
        //Je la mets dans une stream
        ImageInputStream is = ImageIO.createImageInputStream(file);
        //Je crée un pointeur sur cette stream
        Iterator iter = ImageIO.getImageReaders(is);

        //Si ça ne pointe pas sur un fichier je sors du programme
        if (!iter.hasNext())
        {
            System.out.println("Cannot load the specified file "+ file);
            System.exit(1);
        }
        //Je mets ce pointeur sur une imageReader
        ImageReader imageReader = (ImageReader)iter.next();
        //Je récupère les Inputs de la stream
        imageReader.setInput(is);

        
        //Je crée mon image bufferisée avec ce pointeur sur le début de l'image
        BufferedImage image = imageReader.read(0);

        //Je récupère la taille de l'image
        int height = image.getHeight();
        int width = image.getWidth();

        //Je crée une map qui va contenir 
        // clé = la couleur, valeur = le nombre de pixels contenant cette couleur
        Map<Integer, Integer> m = new HashMap<>();
        for(int i=0; i < width ; i++)
        {
            for(int j=0; j < height ; j++)
            {
                int rgb = image.getRGB(i, j);
                int[] rgbArr = getRGBArr(rgb);                
                // Filter out grays....                
                if (!isGray(rgbArr)) {                
                    Integer counter = (Integer) m.get(rgb);   
                    if (counter == null)
                        counter = 0;
                    counter++;                                
                    m.put(rgb, counter);                
                }                
            }
        }        
        String colourHex = getMostCommonColour(m);
        System.out.println(colourHex);
    }


    public static String getMostCommonColour(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
              public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
              }
        });    
        Map.Entry me = (Map.Entry )list.get(list.size()-1);
        int[] rgb= getRGBArr((Integer)me.getKey());
        System.out.println(rgb[0]+" "+rgb[1]+" "+rgb[2]);
        //Conversion en hexa decimal
        return Integer.toHexString(rgb[0])+" "+Integer.toHexString(rgb[1])+" "+Integer.toHexString(rgb[2]);        
    }    

    public static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};

  }
    public static boolean isGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance) 
            if (rbDiff > tolerance || rbDiff < -tolerance) { 
                return false;
            }                 
        return true;
    }
}

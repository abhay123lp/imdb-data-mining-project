/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dm_genre_learning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections4.bag.HashBag;

public class IndexEncoder {


    static String pQuote = "[\"].*[\"]";
    static String pTitle = "(^[^ \t\n\r].+)";
    static String pParenYear = "[/(]([0-9]{4,4})[/)]";
    static String pNum = "[0-9]+";
    static String pWs = "[ \r\n\t]+";
    static String strPattern = String.format("%s %s ([0-9]+)( )?".replace(" ", pWs),
            pTitle,pParenYear);
    static String strPatternGenre = String.format("(^.+) %s ([^/(/{]+)( )?".replace(" ", pWs),
            pParenYear);
    static String strPatternPlot1 = String.format("MV: (.+) %s( )?".replace(" ", pWs),
            pParenYear);
    static String strPatternPlot2 = String.format("( |[^ \n\t\r\n]+)*".replace(" ", pWs),
            pTitle,pParenYear);
    static String strPatternPlotBY = String.format("BY.*");
    static Pattern pattern = Pattern.compile(strPattern);
    static Pattern patternGenre = Pattern.compile(strPatternGenre);
    static Pattern patternPlot1 = Pattern.compile(strPatternPlot1);
    static Pattern patternPlot2 = Pattern.compile(strPatternPlot2);
    static Pattern patternPlotBY = Pattern.compile(strPatternPlotBY);

    static public String storeMovies(File listFile, File output,HashMap<Movie,Movie> movies) throws Exception {
        String err = "UNKNOWN";
        if (!listFile.exists()) {            err = "NOT EXISTS==>" + listFile;        }
        if (output.exists()) {            err = "EXISTS==>" + output;        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(listFile)));
        String line;
        Matcher m;
        System.out.println("Parsing " + listFile);
        int count=0;
        Movie mov;

        while ((line = br.readLine()) != null) {

             m = pattern.matcher(line);
             if(m.find()){
                 try {
                    mov = new Movie(m.group(1), Integer.parseInt(m.group(2)));
                    if (movies.containsKey(mov)) {
                        mov = movies.get(mov);//get original
                    } else {
                        movies.put(mov, mov);
                    }
                        mov.db_cnt++;
                 } catch (Exception e) {
                     e.printStackTrace();
                     System.err.format("\n\n%s\nTitle:\t%s\nYear:\t%s\n",    m.group(0),m.group(1),m.group(3));
                     for(int i=0;i<=m.groupCount();i++)     if(m.group(i)!=null)System.err.format("\nGroup %s: \t%s",i,m.group(i));
                 }
//                     System.out.format("\n\n%s\nTitle:\t%s\nYear:\t%s\n",    m.group(0),m.group(1),m.group(3));
//                     for(int i=0;i<=m.groupCount();i++)     if(m.group(i)!=null)System.out.format("\nGroup %s: \t%s",i,m.group(i));

                 count++;
             }else{
                 //System.out.println(line);
             }
        }

        System.out.println("count="+movies.size());
        return err;
    }

    public static String storeGenres(File listFile, File output,HashMap<Movie,Movie> movies) throws Exception{
          String err = "UNKNOWN";
        if (!listFile.exists()) {            err = "NOT EXISTS==>" + listFile;        }
        if (output.exists()) {            err = "EXISTS==>" + output;        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(listFile)));
        String line;
        Matcher m;
        System.out.println("Parsing " + listFile);
        int count=0;
        Movie mov;

        while ((line = br.readLine()) != null) {

            m = patternGenre.matcher(line);
            if (m.find()) {
                try {
                    mov = new Movie(m.group(1), Integer.parseInt(m.group(2)));
                    if (movies.containsKey(mov)) {
                        mov = movies.get(mov);//get original
                    } else {
                        movies.put(mov, mov);
                    }
                        mov.db_cnt++;
                    if (mov.setGenre(m.group(3))) {
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.format("\n\n%s\nTitle:\t%s\nYear:\t%s\n", m.group(0), m.group(1), m.group(2));
                    for (int i = 0; i <= m.groupCount(); i++) {
                        if (m.group(i) != null) {
                            System.err.format("Group %s: \t%s\n", i, m.group(i));
                        }
                    }
                }
                count++;
            } else {
            }
        }

        err+=String.format("count=" + count);

        return err;
    }

    public static String storeKeywords(File listFile, File output,HashMap<Movie,Movie> movies) throws Exception{
          String err = "UNKNOWN";
        if (!listFile.exists()) {            err = "NOT EXISTS==>" + listFile;        }
        if (output.exists()) {            err = "EXISTS==>" + output;        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(listFile)));
        String line;
        Matcher m;
        System.out.println("Parsing " + listFile);
        int count=0;
        Movie mov;

        while ((line = br.readLine()) != null) {

             m = patternGenre.matcher(line);
             if(m.find()){
                 try{
                     mov=new Movie(m.group(1), Integer.parseInt(m.group(2)));
                     if(movies.containsKey(mov)){
//                         System.out.println(mov+"\t\t"+mov.genre+"\t\t\t"+m.group(3));
                         mov=movies.get(mov);//get original
                     }else{
                         movies.put(mov, mov);
                     }
                        mov.db_cnt++;
                        mov.setKeyword(m.group(3));

                 } catch (Exception e) {
                     e.printStackTrace();
                     System.err.format("\n\n%s\nTitle:\t%s\nYear:\t%s\n",    m.group(0),m.group(1),m.group(2));
                     for(int i=0;i<=m.groupCount();i++)     if(m.group(i)!=null)System.err.format("Group %s: \t%s\n",i,m.group(i));
                 }
//                 if(count%17==0){
//                     System.out.format("\n\n%s\nTitle:\t%s\nYear:\t%s\n",    m.group(0),m.group(1),m.group(2));
//                     for(int i=0;i<=m.groupCount();i++)     if(m.group(i)!=null)System.out.format("\nGroup %s: \t%s",i,m.group(i));
//                 }
                 count++;
             }else{
                 //System.out.println(line);
             }
        }

        err+=String.format("count=" + count);

        return err;
    }
    public static String storePlots(File listFile, File output,HashMap<Movie,Movie> movies) throws Exception{
          String err = "UNKNOWN";
        if (!listFile.exists()) {            err = "NOT EXISTS==>" + listFile;        }
        if (output.exists()) {            err = "EXISTS==>" + output;        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(listFile)));
        String line;
        Matcher m;
        System.out.println("Parsing " + listFile);
        int count = 0;
        Movie mov;
        String str = "";
        String[] arr;
        while ((line = br.readLine()) != null) {

            m = patternPlot1.matcher(line);
            if (m.find()) {
                try {
                    str = "";
                    mov = new Movie(m.group(1), Integer.parseInt(m.group(2)));
                    if (!movies.containsKey(mov)) {
                        movies.put(mov, mov);
                    } else {
                        mov = movies.get(mov);//get original
                    }
                        mov.db_cnt++;
                    while ((line = br.readLine()) != null) {
                        m = patternPlotBY.matcher(line);
                        if (m.find()) {
                            break;
                        } else {
                            str += " " + line;
                        }
                    }
                    //if (!movies.containsKey(mov))
                    {
                        m = patternPlot2.matcher(str);
                        if (m.find()) {
                            arr = str.toLowerCase().split("[ /!/\"/#/$/%/&/'/(/)/*/+/,/-/.///:/;/</=/>/?/@/[/\\/]/^/_/`/{/|/}/~]");
                            for (String s : arr) {
                                mov.setPlot(s);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.printf("line=%s\n", line);
                    System.err.format("\n\n%s\nTitle:\t%s\nYear:\t%s\n", m.group(0), m.group(1), m.group(2));
                    for (int i = 0; i <= m.groupCount(); i++) {
                        if (m.group(i) != null) {
                            System.err.format("Group %s: \t%s\n", i, m.group(i));
                        }
                    }
                }
                count++;
            } else {
                //System.out.println(line);
            }
        }

        err+=String.format("count=" + count);

        return err;
    }
    public static String filter(HashMap<Movie,Movie> movies,int limit) throws Exception{
          String err = "UNKNOWN";
    
        int count = 0;
        HashSet<Movie> del = new HashSet<Movie>();
        for (Movie m : movies.keySet()) {
            if ( isValidMovie(m)) {
                //keeper
            } else {
                del.add(m);
            }
        }
        for (Movie m : del) {
            movies.remove(m);
        }
        err += String.format("count=" + count);

        return err;
    }
    public static String filterByGenre(HashMap<Movie,Movie> movies,int limit, Genre genre) throws Exception{
          String err = "UNKNOWN";

        int count = 0;
        HashSet<Movie> del = new HashSet<Movie>();
        for (Movie m : movies.keySet()) {
            if (m.genre.contains(genre) && isValidMovie(m)) {
                //keeper
            } else {
                del.add(m);
            }
        }
        for (Movie m : del) {
            movies.remove(m);
        }
        err += String.format("count=" + count);

        return err;
    }
    
    public static String select(File output,Genre genre, HashMap<Movie,Movie> movies,int limit) throws Exception{
          String err = "UNKNOWN";
        if (output.exists()) {            err = "EXISTS==>" + output;        }

        int count = 0;
        String line;
        BufferedWriter br=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
        
        for (Movie m : movies.keySet()) {
            if (m.genre.contains(genre) && isValidMovie(m)) {

                line = String.format("%s,%s,%s,%s,%s\n".replace("%s", "\"%s\""),
                        m.title, m.year, m.genre, m.keyword, m.plot);
                br.write(line);
                count++;
                if (limit <= count) {
                    break;
                }
            }
        }
        br.close();
        err+=String.format("count=" + count);

        return err;
    }

    public static int size( Genre genre, HashMap<Movie, Movie> movies, int limit) throws Exception {
        int count = 0;
        for (Movie m : movies.keySet()) {
            if (m.genre.contains(genre) && isValidMovie(m)) {
                count++;
                if (limit <= count) {
                    break;
                }
            }
        }

        return Math.min(limit, count);
    }

    private static boolean isValidMovie(Movie m) {
        return m.keyword.size() > 0 && m.year > 1800 && m.year < 2030 &&m.db_cnt>3
                && !(m.genre.contains(Genre.Adult) || m.keyword.contains("sex") || m.keyword.contains("orgasm"));
    }

    public static <E> String writeHistogram(File output, HashBag<E> bag, int limit) throws Exception {
        String err = "UNKNOWN";
        if (output.exists()) {
            err = "EXISTS==>" + output;
        }

        int count = 0;
        String line;
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));

        for (E b : bag.uniqueSet()) {
            line = String.format("\"%s\",\"%s\"\n", b, bag.getCount(b));
            br.write(line);
            count++;
            if (limit <= count) {
                break;
            }
        }
        br.close();
        err += String.format("count=" + count);
        return err;
    }
}
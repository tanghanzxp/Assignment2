import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.lang.model.util.Elements;
import javax.xml.soap.Node;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanghan on 15-10-20.
 */
public class Info2014302580279 {
    public static void main(String args[]) throws IOException {
        Connection.Response res = Jsoup.connect("http://staff.whu.edu.cn/show.jsp?lang=cn&n=huangchi")
                .method(Connection.Method.GET)
                .execute();
        Document doc = res.parse();


        //Jsoup 获取人名
        String name = doc.select("h3.title").text();


        //提取简介
        org.jsoup.select.Elements p = doc.getElementsByTag("p");
        Pattern ptest = Pattern.compile("<[^>]+>");
        Matcher mtest = ptest.matcher(p.toString());
        //消除空格占位符
        String t = "";
        while(mtest.find()){
            t += mtest.replaceAll("");
        }
        Pattern pt = Pattern.compile("&\\w{4};");
        Matcher mt = pt.matcher(t);
        String personinfo = "";
        while(mt.find()){
            personinfo += mt.replaceAll("");
        }


        //匹配电话号码
        Pattern pphone = Pattern.compile("\\p{Digit}{11}");
        Matcher mp = pphone.matcher(personinfo);
        String phone = "";
        while(mp.find()){
            phone += mp.group();
        }

        //匹配邮箱
        Pattern pemail = Pattern.compile("\\w{5,}+(@\\w+)(\\.\\w+)+");
        Matcher me = pemail.matcher(personinfo);
        String email = "";
        while(me.find()) {
            email += me.group();
        }


        String intr = "";
        intr = "姓名：" + name + "\n";
        intr = "电话：" + phone + "\n";
        intr = "邮箱：" + email + "\n";
        intr = "个人简历:" + "\n";
        intr = intr + personinfo;
        //写入文件
        File infofile = new File("info.txt");
        //判断文件是否存在
        if(!infofile.exists()){
            infofile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(infofile);
        fos.write(intr.getBytes());
    }
}

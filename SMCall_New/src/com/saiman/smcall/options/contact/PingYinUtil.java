package com.saiman.smcall.options.contact;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class PingYinUtil {

    public static String getPingYin(String inputString) {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        format.setVCharType(HanyuPinyinVCharType.WITH_V);

 

        char[] input = inputString.trim().toCharArray();

        String output = "";

 

        try {

            for (int i = 0; i < input.length; i++) {

                if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);

                    output += temp[0];

                } else

                    output += java.lang.Character.toString(input[i]);

            }

        } catch (BadHanyuPinyinOutputFormatCombination e) {

            e.printStackTrace();

        }

        return output;

    }

    /** 
43
     * ��ȡ���ִ�ƴ������ĸ��Ӣ���ַ����� 
44
     * @param chinese ���ִ� 
45
     * @return ����ƴ������ĸ 
46
     */  

    public static String getFirstSpell(String chinese) {  

            StringBuffer pybf = new StringBuffer();  

            char[] arr = chinese.toCharArray();  

            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  

            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  

            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  

            for (int i = 0; i < arr.length; i++) {  

                    if (arr[i] > 128) {  

                            try {  

                                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);  

                                    if (temp != null) {  

                                            pybf.append(temp[0].charAt(0));  

                                    }  

                            } catch (BadHanyuPinyinOutputFormatCombination e) {  

                                    e.printStackTrace();  

                            }  

                    } else {  

                            pybf.append(arr[i]);  

                  }  

           }  

            return pybf.toString().replaceAll("\\W", "").trim();  

    }  

    /** 
70
     * ��ȡ���ִ�ƴ����Ӣ���ַ����� 
71
     * @param chinese ���ִ� 
72
     * @return ����ƴ�� 
73
     */  

    public static String getFullSpell(String chinese) {  

            StringBuffer pybf = new StringBuffer();  

            char[] arr = chinese.toCharArray();  

            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  

            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  

            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  

            for (int i = 0; i < arr.length; i++) {  

                    if (arr[i] > 128) {  

                            try {  

                                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);  

                            } catch (BadHanyuPinyinOutputFormatCombination e) {  

                                    e.printStackTrace();  

                            }  

                    } else {  

                            pybf.append(arr[i]);  

                   }  

            }  

            return pybf.toString();  

    } 

}

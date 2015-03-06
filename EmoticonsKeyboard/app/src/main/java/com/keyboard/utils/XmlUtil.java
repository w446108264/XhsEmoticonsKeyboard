package com.keyboard.utils;


import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.bean.EmoticonSetBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * XmlUtil
 * 解析本地xml,但不一定适合你的xml格式
 * assets/wxemoticons.zip/wxemoticons.xml
 */

public class XmlUtil {

    Context mContext;

    public XmlUtil(Context context) {
        this.mContext = context;
    }

    public InputStream getXmlFromAssets(String xmlName) {
        try {
            InputStream inStream = this.mContext.getResources().getAssets().open(xmlName);
            return inStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getXmlFromSD(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream inStream = new FileInputStream(file);
                return inStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public EmoticonSetBean ParserXml(InputStream inStream) {

        String arrayParentKey = "EmoticonBean";
        EmoticonSetBean emoticonSetBean = new EmoticonSetBean();
        ArrayList<EmoticonBean> emoticonList = new ArrayList<EmoticonBean>();
        emoticonSetBean.setEmoticonList(emoticonList);
        EmoticonBean emoticonBeanTemp = null;

        String emoticonFilePath = Environment.getExternalStorageDirectory() + "/wxemoticons/" ;

        boolean isChildCheck = false;

        if (null != inStream) {
            XmlPullParser pullParser = Xml.newPullParser();
            try {
                pullParser.setInput(inStream, "UTF-8");
                int event = pullParser.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {
                    switch (event) {

                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            String skeyName = pullParser.getName();

                            /**
                             * EmoticonBeans data
                             */
                            if (isChildCheck && emoticonBeanTemp != null) {
                                if (skeyName.equals("eventType")) {
                                    try {
                                        String value = pullParser.nextText();
                                        emoticonBeanTemp.setEventType(Integer.parseInt(value));
                                    } catch (NumberFormatException e) {
                                    }
                                } else if (skeyName.equals("iconUri")) {
                                   String value = pullParser.nextText();
                                    emoticonBeanTemp.setIconUri("file://" + emoticonFilePath + value);
                                }else if (skeyName.equals("content")) {
                                    String value = pullParser.nextText();
                                    emoticonBeanTemp.setContent(value);
                                }
                            }
                            /**
                             * EmoticonSet data
                             */
                            else {
                                try {
                                    if (skeyName.equals("name")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setName(value);
                                    }  else if (skeyName.equals("line")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setLine(Integer.parseInt(value));
                                    } else if (skeyName.equals("row")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setRow(Integer.parseInt(value));
                                    }  else if (skeyName.equals("iconUri")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setIconUri(value);
                                    }  else if (skeyName.equals("iconName")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setIconName(value);
                                    } else if (skeyName.equals("isShowDelBtn")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setShowDelBtn(Integer.parseInt(value) == 1);
                                    } else if (skeyName.equals("itemPadding")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setItemPadding(Integer.parseInt(value));
                                    } else if (skeyName.equals("horizontalSpacing")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setHorizontalSpacing(Integer.parseInt(value));
                                    } else if (skeyName.equals("verticalSpacing")) {
                                        String value = pullParser.nextText();
                                        emoticonSetBean.setVerticalSpacing(Integer.parseInt(value));
                                    }
                                } catch (NumberFormatException e) {
                                }
                            }

                            if (skeyName.equals(arrayParentKey)) {
                                isChildCheck = true;
                                emoticonBeanTemp = new EmoticonBean();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            String ekeyName = pullParser.getName();
                            if (isChildCheck && ekeyName.equals(arrayParentKey)) {
                                isChildCheck = false;
                                emoticonList.add(emoticonBeanTemp);
                            }
                            break;
                        default:
                            break;
                    }
                    event = pullParser.next();
                }
                return emoticonSetBean;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return emoticonSetBean;
    }
}

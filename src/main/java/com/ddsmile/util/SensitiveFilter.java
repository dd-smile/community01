package com.ddsmile.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤器
 * 1.定义前缀树；
 * 2.根据敏感词，初始化前缀树；
 * 3.编写过滤敏感词的方法。
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换的符号
    private static final String REPLACEMENT= "***";

    //初始化根节点
    private TrieNode rootNode = new TrieNode();

    //定义前缀树,内部类
    private class TrieNode{
        // 关键词结束标识
        private boolean isKeywordEnd = false;

        // 子节点：一个节点的子节点为多个，因此这里用Map封装
        // key为下级的字符、value是下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 判断是否为结束
         * @return
         */
        public boolean isKeywordEnd(){
            return isKeywordEnd;
        }

        /**
         * 设置结束标识
         * @param keywordEnd 关键词结束标识
         */
        public void setKeywordEnd(boolean keywordEnd){
            isKeywordEnd = keywordEnd;
        }

        /**
         * 添加子节点
         * @param c 下级字符
         * @param node 下级节点
         */
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        /**
         * 获取子节点
         * @param c 下级字符
         * @return
         */
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }

    /**
     *  初始化方法
     *  当容器实例化Bean(调用其构造器)后，会自动调用该方法
     *  此Bean在服务器启动时，即被初始化
     */
    @PostConstruct
    public void init(){
        try(
                //自动加finally并关闭
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //将字节流转化为字符流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            //读取敏感词并添加到前缀树
            String keyword;
            while((keyword = reader.readLine()) != null){
                this.addKeyword(keyword);
            }
        }catch (IOException e){
            logger.error("加载敏感词失败：", e.getMessage());
        }
    }

    /**
     * 将敏感词添加到前缀树
     * @param keyword 读取到的敏感词
     */
    private void addKeyword(String keyword){
        //默认指向根节点
        TrieNode tempNode = rootNode;

        for (int i=0; i<keyword.length(); i++){
            char c = keyword.charAt(i);
            //根据字符获取下一个节点，如果没有，再新创建一个节点
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            //指向子节点，进入下一轮循环
            tempNode = subNode;
            //设置结束标识
            if(i==keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针一：指向树的各个节点
        TrieNode tempNode = rootNode;
        //指针二、指针三：指向待过滤字符串
        int begin = 0;
        int position = 0;
        //过滤后的文本
        StringBuilder sb = new StringBuilder();

        while (position < text.length()){
            char c = text.charAt(position);
            //跳过符号
            if(isSymbol(c)) {
                //若指针一处于根节点，将此符号计入结果，让指针2向下走一步
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头还是中间，指针三都向下走一步
                position++;
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //树的指针重新指向根节点，进行下一轮判断
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词：将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                //树的指针重新指向根节点
                tempNode = rootNode;
            }else {
                //检查下一个字符
                position++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 判断是否为符号
     * @param c 字符
     * @return
     */
    private boolean isSymbol(Character c){
        // 0x2E80~0x9FFF为东亚文字范围（日韩中等）
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


}

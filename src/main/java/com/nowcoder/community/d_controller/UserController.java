package com.nowcoder.community.d_controller;

import com.nowcoder.community.a_entity.User;
import com.nowcoder.community.c_service.UserService;
import com.nowcoder.community.z_util.CommunityUtil;
import com.nowcoder.community.z_util.HostHolder;
import com.nowcoder.community.z_util.annotation.LoginRequired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * <p>——————————————————————————————————————————————-————
 * <p>   【名字】user控制器 【所属包】com.nowcoder.community.d_controller
 * <p>
 * <p>   【谁调用我】点击首页的用户头像下的修改个人信息按钮
 * <p>
 * <p>   【调用我干什么】修改个人信息
 * <p>——————————————————————————————————————————————-————
 */
@Controller
@RequestMapping("/user")
public class UserController {
    //appop配置里拿路径
    @Value("${path.upload}")
    private String uploadPath;

    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】UserController
     * <p>
     * <p>   【谁调用我】第一次点修改个人信息-get
     * <p>
     * <p>   【调用我干什么】返回修改表单的页面
     * <p>——————————————————————————————————————————————-————
     */
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSetPage(Model model){
        return "moban/site/setting";
    }
    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】UserController
     * <p>
     * <p>   【谁调用我】修改表单提交头像按钮
     * <p>
     * <p>   【调用我干什么】校验头像传入是否为空，并修改数据库头像url（后期还要把头像改成验证码一样的void-src）
     * <p>——————————————————————————————————————————————-————
     */
    @LoginRequired
    @RequestMapping("/upload")// /r/user类/upload方法
    public String updateTouxiangIMG(MultipartFile headerImage, Model model){//错在img参数和html的name不同名
        //提交的图片一定不空，有html的require保证，所以这里不用判null-imgM返回
        System.out.println(headerImage);
        //判断是不是没有后缀的文件【这种文件肯定不是图片】
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        if(!suffix.equals(".png")){//blank是代表全是空格
            model.addAttribute("imgM","提交的文件为不是png格式，我们只收png图片");
            return "moban/site/setting";
        }

        //1 格式没问题，开始生成新图片文件名
        File dest = new File(uploadPath + "/" + fileName);
        //2 存本地文件，练下
        try {
            headerImage.transferTo(dest);//这句话太强大了，直接存本地
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        //3 修改数据库的url访问路径，后面咋访问那张图片？-那个src类似验证码咋做？-答：用 fis = new FileInputStream(
        //先拿当前user，好定位，在改url
        User user= hostHolder.getUser();
        //这是假的云盘url，后面src拿图片，只取/header后面的filename
        String newimgURL = "http://localhost:8080/r" + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), newimgURL);


        //4完事，可以返回首页了，之后写src码
        return "redirect:/index";//重新自己加载

    }
    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】UserController（没有indexCon，所以就放这里吧。。）
     * <p>
     * <p>   【谁调用我】index首页的自动拿头像-类似验证码
     * <p>
     * <p>   【调用我干什么】拿活头像
     * <p>——————————————————————————————————————————————-————
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getIMG(@PathVariable("fileName") String fileName,
                       HttpServletResponse response){
        //生抄，扣没意义，后面用云盘
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);

        //本地先拆成流，在“画”在网页！惊了！
        try (
            FileInputStream fis = new FileInputStream(fileName);//(不用final关)
            OutputStream os = response.getOutputStream();
        ){
            byte[] buf = new byte[1024];
            int b =0;
            while((b=fis.read(buf))!=-1) {
                os.write(buf, 0, b);//本地先拆成流，在“画”在网页！惊了！
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //下面首次自己开发修改密码-2021年2月22日17:52:16
    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】UserController
     * <p>
     * <p>   【谁调用我】个人设置修改密码
     * <p>
     * <p>   【调用我干什么】修改密码
     * <p>——————————————————————————————————————————————-————
     */
    @LoginRequired
    @RequestMapping(path =  "/gaiPassword",method = RequestMethod.POST)
    public String f(Model model,String oldpassword,String newpassword){
        //如果原密码错误，设置页码跳回，显示MSg
        //老密码直接用TL拿！ticket比
        User user = hostHolder.getUser();
        String password = user.getPassword();//有了TL的1轮，之后就可以比实际password了
        //错1：加密之后才能比
        String jiamihouPass = CommunityUtil.md5(oldpassword + user.getSalt());//mh新颖点
        if(!password.equals(jiamihouPass))
        {
            model.addAttribute("passM","原密码错误，仔细回忆！");
            return "moban/site/setting";
        }

        //原密码正确了，下面可以改新密码了
        //拼接newpassword，（注意salt不变用原库里的）
        String tmp = newpassword+user.getSalt();
        String newMD5pass = CommunityUtil.md5(tmp);
        userService.updatePasswordByid(user.getId(), newMD5pass);

        return "redirect:/index";
    }


}

package com.zbf.user.web;

import com.zbf.user.entity.BaseMenu;
import com.zbf.user.service.IBaseMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: zhuyiwen
 * 作者: zhuyiwen
 * 日期: 2020/9/16 18:36
 * 描述:
 */
@RestController
public class MenuController {

    @Autowired
    private IBaseMenuService iBaseMenuService;

    /**
     * 查询所有的菜单
     * @param loginName
     * @return
     */
    @RequestMapping("list")
    public List<BaseMenu> list(String loginName){
        return iBaseMenuService.getMenuList(loginName);
    }


    @RequestMapping("menuAll")
    public List<BaseMenu> menuAll(){
        return iBaseMenuService.menuAll();
    }

    /**
     * 添加菜单
     * @param baseMenu
     * @return
     */
    @RequestMapping("addMenu")
    public boolean addMenu(@RequestBody BaseMenu baseMenu){
        Integer leval = baseMenu.getLeval();
        long lv = leval.longValue();
        System.out.println(lv);
        if(lv == 1){
            baseMenu.setParentCode(0L);
            baseMenu.setId(Long.valueOf(time()));
            baseMenu.setCode(Long.valueOf(time())+1);
            baseMenu.setCreateTime(new Date());
        }else{
            baseMenu.setId(Long.valueOf(time()));
            baseMenu.setCode(Long.valueOf(time())+1);
            baseMenu.setCreateTime(new Date());
        }
        iBaseMenuService.saveOrUpdate(baseMenu);
        return true;
    }

    /**
     * 修改菜单
     * @param baseMenu
     * @return
     */
    @RequestMapping("updMenu")
    public boolean updMenu(@RequestBody BaseMenu baseMenu){
        boolean b = iBaseMenuService.saveOrUpdate(baseMenu);
        return b;
    }

    /**
     * 删除菜单
     * @param baseMenu
     * @return
     */
    @RequestMapping("delMenu")
    public boolean delMenu(@RequestBody BaseMenu baseMenu){
        boolean b = iBaseMenuService.removeById(baseMenu.getId());
        return b;
    }

    public String time(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format = simpleDateFormat.format(date);
        String response = format.replaceAll("[[\\s-:punct:]]","");
        return response;
    }
}

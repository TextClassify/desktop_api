## textClassify 桌面端后台服务（简陋版）

**说明:**
1. clone项目本地是构建不起来的，pom文件中依赖了我自己发布到本地仓库的jar（jar包在libs目录中）
2. aop进行接口调用日志记录、全局异常处理、返回结果格式统一
3. 最新一版引入了百度文本分类api服务，老接口仍然可以使用
4. 增加了用户中心功能、token、分享分类文章等操作
5. 待解决token过期处理、分页查询和排序

### 新接口

|功能描述| 接口地址 |
| :---:| :---- |
| [用户登陆](https://github.com/TextClassify/desktop_api/blob/master/README.md#5.用户登陆) | http://120.24.66.39:8088/user/login |
| 用户注册 | http://120.24.66.39:8088/user/register |
| 用户更新 | http://120.24.66.39:8088/user/update |
| 一篇文章分类 | http://120.24.66.39:8088/user/oneText |
| 分享一篇文章 | http://120.24.66.39:8088/user/shareArticle |
| 删除一篇文章 | http://120.24.66.39:8088/user/deleteArticle |
| 所有用户分享的文章 | http://120.24.66.39:8088/user/allSharingArticles |
| 查自己分享的所有文章 | http://120.24.66.39:8088/user/allUserSharedArticles |
| 某用户分享的所有文章 | http://120.24.66.39:8088/user/userSharingArticles |
| 查自己的所有云端文章 | http://120.24.66.39:8088/user/allUserArticles |


**接口数据字段说明**
1. article实体中tag为一级标签(一定有)，tag1、tag2、tag3为二级标签(可能没有)
2. article的share为1表示处于分享状态、0表示私有状态
3. article的state为0表示处于有效状态、1表示被删除(垃圾站)
4. owerId为文章拥有者id，不需要保存
5. code码对照表如下

|code码| 含义说明 |
| :---:| :---- |
| 0 | 成功 |
| 1 | 未知错误 |
| 2 | 系统异常 |
| 3 | 请求地址不存在 |
| 10 | 登陆失败 |
| 11 | 用户名已存在 |
| 12 | token不合法 |
| 13 | 没有权限分享该文章 |
| 14 | 没有权限删除该文章 |
| 15 | 用户名和token不一致 |

**详细接口说明**
    
#### 5.用户登陆
      路径：http://120.24.66.39:8088/user/login
      参数：{
         	"userName":"xiaowang",
         	"password":"wangyonghao"
         }
      参数说明：userName和passsword必须传递
      成功返回：{
               "code": 0,
               "msg": "登陆成功",
               "data": {
                   "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvd2FuZyIsInVpZCI6NCwia2V5R2VuIjoiMC4yNjM4MTE2NTIxOTAzODA4MyJ9.F3uQNRRL4ReV0aPDii9UylIa4forA6Z94qNBOzemVH1EqgL3B0ctrH4XG1Ysd5hbeCsR91mlTtXx9s7P447Gtg",
                   "user": {
                       "id": 4,
                       "userName": "xiaowang",
                       "password": "wangyonghao",
                       "email": "643710049@qq.com",
                       "information": null,
                       "lastLogin": "Wed Jun 13 12:46:20 CST 2018",
                       "access_token": null
                   },
                   "articles": [
                       {
                           "id": 9,
                           "title": "欧洲冠军联赛",
                           "content": "欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。",
                           "tag": "体育",
                           "tag1": "足球",
                           "tag2": "国际足球",
                           "tag3": "英超",
                           "share": 1,
                           "date": "Tue Jun 12 23:23:05 CST 2018",
                           "state": 1,
                           "owerId": 4,
                           "access_token": null
                       }
                   ]
               }
           }
      失败返回：{
               "code": 10,
               "msg": "用户名或密码错误",
               "data": null
           }
    
6.功能：用户注册 

      路径：http://120.24.66.39:8088/user/register
      参数：{
         	"userName":"xiaoluo",
         	"password":"123456",
         	"email":"643710049@qq.com",
         	"information":"我就是傻逼"
         }
      参数说明：userName和password必须传
      成功返回：{
               "code": 0,
               "msg": "注册成功",
               "data": {
                   "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFsdW8iLCJ1aWQiOjEyLCJrZXlHZW4iOiIwLjAzNTA0ODM1NDk5Nzg3MTUxIn0.5aujxnRxhvO50C22yrWUkM-26kwf2uVMfpsWw2-l4ZfT_Dcrnlme4xcJEf9Aojm5FYqBXmtfUqjHBQT_cczpgw",
                   "user": {
                       "id": 12,
                       "userName": "xialuo",
                       "password": "123456",
                       "email": "643710049@qq.com",
                       "information": "我就是傻逼",
                       "lastLogin": "Wed Jun 13 13:00:26 CST 2018",
                       "access_token": null
                   }
               }
           }
      失败返回：{
               "code": 11,
               "msg": "用户名已存在",
               "data": null
           }

    7.功能：更新用户信息
      路径：http://120.24.66.39:8088/user/update
      参数：{
         	"userName":"xiaoluo",
         	"password":"123456",
         	"email":"643710049@qq.com",
         	"information":"我就是大佬",
         	"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvbHVvIiwidWlkIjoxMSwia2V5R2VuIjoiMC45NzI1MDE3Njc1Nzk2MDA2In0.pYs3HXUsiO-Ln9WxBGiLBgchYUVTJ0dlqdfqc0uRyehDWglXdzJpW7yaFP7K8Wak9Xhx8rA1I_Dp_pjDf_pW-Q"
         }
      参数说明：token必须传递，用户名不能修改也必须传递
      成功返回：{
               "code": 0,
               "msg": "更新用户信息成功",
               "data": {
                   "user": {
                       "id": 11,
                       "userName": "xiaoluo",
                       "password": "123456",
                       "email": "643710049@qq.com",
                       "information": "我就是大佬",
                       "lastLogin": "Wed Jun 13 13:05:16 CST 2018",
                       "access_token": null
                   }
               }
           }
      失败返回：{
               "code": 12,
               "msg": "token不合法",
               "data": null
           }
           或则
           {
               "code": 15,
               "msg": "用户名和token不一致",
               "data": null
           }
    
    8.功能：用户获取一篇文章的分类
      路径：http://120.24.66.39:8088/user/oneText
      参数：{
         	"content":"欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。",
         	"title":"欧洲冠军联赛",
         	"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvd2FuZyIsInVpZCI6NCwia2V5R2VuIjoiMC4yNjk0NDg3NTUxMDA5MTAyNiJ9.RHYR2ARfIdiruMSJP7GzIduKjqM0VlBmrJdpTWw9Vv0Kg07sWoLhd9UAaI4h82fjcyQ8bjHE84cizVZcO0ed2w"
         }
      参数说明：全是必须参数
      成功返回：{
               "code": 0,
               "msg": "成功分类",
               "data": {
                   "article": {
                       "id": 9,
                       "title": "欧洲冠军联赛",
                       "content": "欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。",
                       "tag": "体育",
                       "tag1": "足球",
                       "tag2": "国际足球",
                       "tag3": "英超",
                       "share": 1,
                       "date": "Tue Jun 12 23:23:05 CST 2018",
                       "state": 1,
                       "owerId": 4,
                       "access_token": null
                   }
               }
           }
      失败返回：{
               "code": 12,
               "msg": "token不合法",
               "data": null
           }
    
    9.功能：用户分享一篇文章
      路径：http://120.24.66.39:8088/user/shareArticle
      参数：{
         	"id":8,
         	"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvbHVvIiwidWlkIjoxMSwia2V5R2VuIjoiMC45NzI1MDE3Njc1Nzk2MDA2In0.pYs3HXUsiO-Ln9WxBGiLBgchYUVTJ0dlqdfqc0uRyehDWglXdzJpW7yaFP7K8Wak9Xhx8rA1I_Dp_pjDf_pW-Q"
         }
      参数说明：id为文章id，全是必传递参数
      成功返回：{
               "code": 0,
               "msg": "分享成功",
               "data": {
                   "id": 8,
                   "title": "G7峰会今天在加拿大举行",
                   "content": "特朗普很牛逼，默克尔垃圾",
                   "tag": "国际",
                   "tag1": null,
                   "tag2": null,
                   "tag3": null,
                   "share": 1,
                   "date": "Tue Jun 12 23:00:08 CST 2018",
                   "state": 0,
                   "owerId": 11,
                   "access_token": null
               }
           }
      失败返回：{
               "code": 13,
               "msg": "你没有权限分享这篇文章",
               "data": null
           }
           或则
           {
               "code": 12,
               "msg": "token不合法",
               "data": null
           }
           或则（//TODO 文章不存在异常友好提示）
           {
               "code": 2,
               "msg": "系统异常，请联系老王",
               "data": null
           }
    
    10.功能：用户删除一篇文章
       路径：http://120.24.66.39:8088/user/deleteArticle
       参数：{
          	"id":8,
          	"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvbHVvIiwidWlkIjoxMSwia2V5R2VuIjoiMC45NzI1MDE3Njc1Nzk2MDA2In0.pYs3HXUsiO-Ln9WxBGiLBgchYUVTJ0dlqdfqc0uRyehDWglXdzJpW7yaFP7K8Wak9Xhx8rA1I_Dp_pjDf_pW-Q"
          }
       参数说明：id为文章id，参数全必须传递
       成功返回：{
                "code": 0,
                "msg": "删除成功",
                "data": null
            }
       失败返回：{
                "code": 14,
                "msg": "你没有权限删除这篇文章",
                "data": null
            }
            或则
            {
                "code": 12,
                "msg": "token不合法",
                "data": null
            }
            或则（//TODO 文章不存在异常友好提示）
            {
                "code": 2,
                "msg": "系统异常，请联系老王",
                "data": null
            }
    
    11.功能：查看所有用户分享的文章（//TODO 分页和排序）//TODO 有待考究是否需要全开放
       路径：http://120.24.66.39:8088/user/allSharingArticles
       参数：{
          		"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvd2FuZyIsInVpZCI6NCwia2V5R2VuIjoiMC4yMzE2NDgyNTgyMjk1Nzc0NiJ9.yzWBATnJ8zSqlTWw7LoWZNblMIaYSEYkSzANHqXBvEyKcRw0aDjuGsiUTD-LwtPPTH-5S6aIlRV_CB__kLEPug"
          }
       参数说明：token必传递
       成功返回：{
                "code": 0,
                "msg": "成功",
                "data": {
                    "articles": [
                        {
                            "id": 9,
                            "title": "欧洲冠军联赛",
                            "content": "欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。",
                            "tag": "体育",
                            "tag1": "足球",
                            "tag2": "国际足球",
                            "tag3": "英超",
                            "share": 1,
                            "date": "Tue Jun 12 23:23:05 CST 2018",
                            "state": 1,
                            "owerId": 4,
                            "access_token": null
                        },
                        {
                            "id": 8,
                            "title": "G7峰会今天在加拿大举行",
                            "content": "特朗普很牛逼，默克尔垃圾",
                            "tag": "国际",
                            "tag1": null,
                            "tag2": null,
                            "tag3": null,
                            "share": 1,
                            "date": "Tue Jun 12 23:00:08 CST 2018",
                            "state": 1,
                            "owerId": 11,
                            "access_token": null
                        }
                    ]
                }
            }
       失败返回：{
                "code": 12,
                "msg": "token不合法",
                "data": null
            }
    
    12.功能：查看自己分享的所有文章
       路径：http://120.24.66.39:8088/user/allUserSharedArticles
       参数：{
          	"userName":"xiaowang",
          	"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvd2FuZyIsInVpZCI6NCwia2V5R2VuIjoiMC4yOTMzODQxMzg2MjYzMjUzIn0.DpPu1LzHLW33248_8LYh02PpO2fdwuvvUeYwFc1ijoQztJBz12MYwQrAtZ8A7AhS9jy5FZqtTIQ4VRQi8B2Pyg"
          }
       参数说明：userName和token必传递
       成功返回：{
                "code": 0,
                "msg": "成功",
                "data": {
                    "articles": [
                        {
                            "id": 9,
                            "title": "欧洲冠军联赛",
                            "content": "欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。",
                            "tag": "体育",
                            "tag1": "足球",
                            "tag2": "国际足球",
                            "tag3": "英超",
                            "share": 1,
                            "date": "Tue Jun 12 23:23:05 CST 2018",
                            "state": 1,
                            "owerId": 4,
                            "access_token": null
                        }
                    ]
                }
            }
       失败返回：{
                "code": 15,
                "msg": "用户名和token不一致",
                "data": null
            }
            或则
            {
                "code": 12,
                "msg": "token不合法",
                "data": null
            }
    
    13.功能：查看某用户分享的所有文章
           路径：http://120.24.66.39:8088/user/userSharingArticles
           参数：{
              	"userName":"xiaowang"
              }
           参数说明：userName指被查询的用户名，不用传递token
           成功返回：{
                    "code": 0,
                    "msg": "成功",
                    "data": {
                        "articles": [
                            {
                                "id": 9,
                                "title": "欧洲冠军联赛",
                                "content": "欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。",
                                "tag": "体育",
                                "tag1": "足球",
                                "tag2": "国际足球",
                                "tag3": "英超",
                                "share": 1,
                                "date": "Tue Jun 12 23:23:05 CST 2018",
                                "state": 1,
                                "owerId": 4,
                                "access_token": null
                            }
                        ]
                    }
                }
           失败返回（TODO：用户名不存在异常友好提示）：
                {
                    "code": 2,
                    "msg": "系统异常，请联系老王",
                    "data": null
                }
    
    14.功能：查看自己所有的文章
       路径：http://120.24.66.39:8088/user/allUserArticles
       参数：{
          	"userName":"xiaowang",
          	"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvd2FuZyIsInVpZCI6NCwia2V5R2VuIjoiMC4yOTMzODQxMzg2MjYzMjUzIn0.DpPu1LzHLW33248_8LYh02PpO2fdwuvvUeYwFc1ijoQztJBz12MYwQrAtZ8A7AhS9jy5FZqtTIQ4VRQi8B2Pyg"
          }
       参数说明：userName和token必须传递
       成功返回：{
                "code": 0,
                "msg": "成功",
                "data": {
                    "articles": [
                        {
                            "id": 9,
                            "title": "欧洲冠军联赛",
                            "content": "欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。",
                            "tag": "体育",
                            "tag1": "足球",
                            "tag2": "国际足球",
                            "tag3": "英超",
                            "share": 1,
                            "date": "Tue Jun 12 23:23:05 CST 2018",
                            "state": 1,
                            "owerId": 4,
                            "access_token": null
                        }
                    ]
                }
            }
       失败返回：{
                "code": 15,
                "msg": "用户名和token不一致",
                "data": null
            }
            或则
            {
                "code": 12,
                "msg": "token不合法",
                "data": null
            }
    
    
    
    1. 功能：获取一篇文章的分类
      路径：http://120.24.66.93:8088/api/oneText
      参数：{'text': '再次回到世锦赛的赛场上，林丹终于变回了以前的那个超级丹'}
      如果请求头包含token字段，将会记录用户数据
      参数说明：key必须是text
      返回结果：{
               "code": 0,
               "msg": "成功.",
               "data": {
                   "社会": 0.11774863335479152,
                   "时政": 0.6057931596477106,
                   "股票": 0.1772069315953813
               }
           }
      结果说明：数据格式：json。返回3种最可能得分类以及对应分类得概率。
      
    2. 功能：获取多篇文章的分类
      路径：http://120.24.66.93:8088/api/someText
      参数：{ 0: '再次回到世锦赛的赛场上，林丹终于变回了以前的那个超级丹',
              1: '再次回到世锦赛的赛场上，林丹终于变回了以前的那个超级丹',
              2: '再次回到世锦赛的赛场上，林丹终于变回了以前的那个超级丹'
              ... ...}
      参数说明：key没有限制，最好使用0123
      返回结果：{
               "code": 0,
               "msg": "成功.",
               "data": {
                   "0": {
                       "体育": 0.4349721445339052,
                       "股票": 0.10582441409605994,
                       "游戏": 0.43198942192079215
                   },
                   "1": {
                       "体育": 0.4349721445339052,
                       "股票": 0.10582441409605994,
                       "游戏": 0.43198942192079215
                   },
                   "2": {
                       "体育": 0.4349721445339052,
                       "股票": 0.10582441409605994,
                       "游戏": 0.43198942192079215
                   }
               }
           }
      结果说明：返回对应每篇文章的3中可能分类及其概率
      
    3.功能：验证用户登陆合法性
     路径：http://120.24.66.93:8088/api/login
     参数：{
       	 "userName":"laowang",
       	 "password":"123",
       	  }
     返回结果：
       响应头包含token字段
       `Authorization →Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvd2FuZyIsImtleUdlbiI6IiJ9.y0l0zA7NLT-0O1IGPVDwsrdv2t2ca640QuXDQo80gEwzUQNpfO1aAXL1A2pMFebikZfcsVuue2x6W8F4h2RhSA`
       合法：{
              "code": 0,
              "msg": "成功.",
              "data": "laowang登陆成功"
            }
       不合法：{
               "code": 10,
               "msg": "登陆失败",
               "data": null
             }
             
             
    4. 功能；用户注册
      路径：http://120.24.66.93:8088/api/register
      参数： {
        	"userName":"laowang",
        	"password":"1234",
        	"email":"643710049@qq.com"
           }
      返回结果：
        成功： {
                "code": 0,
                "msg": "成功.",
                "data": "王皓注册成功"
             }
        失败：{
               "code": 11,
               "msg": "用户已存在",
               "data": null
             }
    
    

_返回其他结果说明:_

      1. 算法jar那边的 
      {
                 "code": 1,
                 "msg": "未知的错误.",
                 "data": null
      }
      2. 后台系统、请求数据库中不存在的数据等 
      {
                  "code": 2,
                  "msg": "系统异常，请联系老王.",
                  "data": null
      }
      3. 请求路径不存在
      {
                  "code": 3,
                  "msg": "请求地址:/api/oneText1不存在",
                  "data": null
      }
添加token方式截图

**TODO**
- ~~加token~~
- ~~全局日志记录~~
- ~~重构优化(请求结果格式统一，系统异常处理)~~
- ~~用户中心完善，包含用户分类文章分享功能~~
- ~~文本分类核心算法改用百度的~~
- 完成项目TODO
- 查询分页、排序显示功能
- 批量文章操作
- 网络文章操作（~~->智能化爬虫~~）
- 用户文章回收站功能
- 文章收藏、点赞、评论功能
- 改用多线程、消息机制处理耗时任务
- 分类算法更替为算法团队那边的
- 删除本项目
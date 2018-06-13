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
| [用户登陆](https://github.com/TextClassify/desktop_api/blob/master/README.md#5用户登陆) | /user/login |
| [用户注册](https://github.com/TextClassify/desktop_api/blob/master/README.md#6用户注册) | /user/register |
| [用户更新](https://github.com/TextClassify/desktop_api/blob/master/README.md#7更新用户信息) | /user/update |
| [一篇文章分类](https://github.com/TextClassify/desktop_api/blob/master/README.md#8用户获取一篇文章的分类) | /user/oneText |
| [分享一篇文章](https://github.com/TextClassify/desktop_api/blob/master/README.md#9用户分享一篇文章) | /user/shareArticle |
| [删除一篇文章](https://github.com/TextClassify/desktop_api/blob/master/README.md#10用户删除一篇文章) | /user/deleteArticle |
| [所有用户分享的文章](https://github.com/TextClassify/desktop_api/blob/master/README.md#11查看所有用户分享的文章) | /user/allSharingArticles |
| [查自己分享的所有文章](https://github.com/TextClassify/desktop_api/blob/master/README.md#12查看自己分享的所有文章) | /user/allUserSharedArticles |
| [某用户分享的所有文章](https://github.com/TextClassify/desktop_api/blob/master/README.md#13查看某用户分享的所有文章) | /user/userSharingArticles |
| [查自己的所有云端文章](https://github.com/TextClassify/desktop_api/blob/master/README.md#14查看自己所有的文章) | /user/allUserArticles |
| [用户获取网络文章分类](https://github.com/TextClassify/desktop_api/blob/master/README.md#15用户获取网络文章分类) | /user/netArticle?url={url} |
| [获取网络文章分类](https://github.com/TextClassify/desktop_api/blob/master/README.md#16获取网络文章分类) | /api/netArticle?url={url} |

 [老接口](https://github.com/TextClassify/desktop_api/blob/master/README.md#老版本接口)采用清华大学提供的文本分类算法[THUCTC](https://github.com/thunlp/THUCTC)

__注意：只有第16个接口是get方法(返回数据格式特殊)，其他全是post__

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
| 16 | 网络文章解析失败 |

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
    
#### 6.用户注册 
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

#### 7.更新用户信息
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
    
#### 8.用户获取一篇文章的分类
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
    
#### 9.用户分享一篇文章
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
    
#### 10.用户删除一篇文章
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
    
#### 11.查看所有用户分享的文章
      （//TODO 分页和排序）//TODO 有待考究是否需要全开放
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
    
#### 12.查看自己分享的所有文章
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
    
#### 13.查看某用户分享的所有文章
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
    
#### 14.查看自己所有的文章
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
#### 15.用户获取网络文章分类
      路径：http://120.24.66.39:8088/user/netArticle?url={url}
      参数：{
         	"access_token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4aWFvd2FuZyIsInVpZCI6Mywia2V5R2VuIjoiMC4yNzE5MDA4NzU3OTA3ODAxNSJ9.o-eR3tJfquYDOLgjHLZCjmN7p3bWoZa22PWGZkIcsaOmXCUu1TQuGGPUfv_eR_y8YrfU9GfRPX-kPQKOcgwKCw"
         }
      返回结果参见 接口8[一篇文章分类]
      
#### 16.获取网络文章分类
      路径：http://120.24.66.39:8088/api/netArticle?url={url}
      方法：get
      返回结果：{
               "content": "“自力更生是中华民族自立于世界民族之林的奋斗基点，自主创新是我们攀登世界科技高峰的必由之路。”在今年的两院院士大会上，“创新”成为这一科技盛会的重要关键词，被习近平总书记提及128次。梳理习近平在此次大会上的科技“新语”，一幅新时代创新中国、科技强国的宏伟蓝图在世人面前徐徐展开。\n\n要增强“四个自信”，以关键共性技术、前沿引领技术、现代工程技术、颠覆性技术创新为突破口，敢于走前人没走过的路，努力实现关键核心技术自主可控，把创新主动权、发展主动权牢牢掌握在自己手中。\n\n——把满足人民对美好生活的向往作为科技创新的落脚点\n\n要把满足人民对美好生活的向往作为科技创新的落脚点，把惠民、利民、富民、改善民生作为科技创新的重要方向。\n\n要加大应用基础研究力度，以推动重大科技项目为抓手，打通“最后一公里”，拆除阻碍产业化的“篱笆墙”，疏通应用基础研究和产业化连接的快车道，促进创新链和产业链精准对接，加快科研成果从样品到产品再到商品的转化，把科技成果充分应用到现代化事业中去。\n\n广大工程科技工作者既要有工匠精神，又要有团结精神，围绕国家重大战略需求，瞄准经济建设和事关国家安全的重大工程科技问题，紧贴新时代社会民生现实需求和军民融合需求，加快自主创新成果转化应用，在前瞻性、战略性领域打好主动仗。\n\n——自主创新是开放环境下的创新，要聚四海之气、借八方之力\n\n不拒众流，方为江海。自主创新是开放环境下的创新，绝不能关起门来搞，而是要聚四海之气、借八方之力。\n\n全面深化科技体制改革，提升创新体系效能，着力激发创新活力。创新决胜未来，改革关乎国运。科技领域是最需要不断改革的领域。\n\n要坚持科技创新和制度创新“双轮驱动”，以问题为导向，以需求为牵引，在实践载体、制度安排、政策保障、环境营造上下功夫，在创新主体、创新基础、创新资源、创新环境等方面持续用力，强化国家战略科技力量，提升国家创新体系整体效能。\n\n要坚持以全球视野谋划和推动科技创新，全方位加强国际科技创新合作，积极主动融入全球科技创新网络，提高国家科技计划对外开放水平，积极参与和主导国际大科学计划和工程，鼓励我国科学家发起和组织国际科技合作计划。\n\n要把“一带一路”建成创新之路，合作建设面向沿线国家的科技创新联盟和科技创新基地，为各国共同发展创造机遇和平台。要最大限度用好全球创新资源，全面提升我国在全球创新格局中的位势，提高我国在全球科技治理中的影响力和规则制定能力。\n\n——创新之道，唯在得人\n\n创新之道，唯在得人。得人之要，必广其途以储之。要营造良好创新环境，加快形成有利于人才成长的培养机制、有利于人尽其才的使用机制、有利于竞相成长各展其能的激励机制、有利于各类人才脱颖而出的竞争机制，培植好人才成长的沃土，让人才根系更加发达，一茬接一茬茁壮成长。\n\n——青年一代有理想、有本领、有担当，科技就有前途，创新就有希望\n\n青年是祖国的前途、民族的希望、创新的未来。青年一代有理想、有本领、有担当，科技就有前途，创新就有希望。\n\n——广大科技工作者要有强烈的创新信心和决心\n\n创新从来都是九死一生，但我们必须有“亦余心之所善兮，虽九死其犹未悔”的豪情。我国广大科技工作者要有强烈的创新信心和决心，既不妄自菲薄，也不妄自尊大，勇于攻坚克难、追求卓越、赢得胜利，积极抢占科技竞争和未来发展制高点。\n\n要尊重人才成长规律，解决人才队伍结构性矛盾，构建完备的人才梯次结构，培养造就一大批具有国际水平的战略科技人才、科技领军人才、青年科技人才和创新团队。要加强人才投入，优化人才政策，营造有利于创新创业的政策环境，构建有效的引才用才机制，形成天下英才聚神州、万类霜天竞自由的创新局面！（整理/李珊珊）",
               "item": {
                   "lv1_tag_list": [
                       {
                           "score": 0.340552,
                           "tag": "时事"
                       }
                   ],
                   "lv2_tag_list": [
                       {
                           "score": 0.790177,
                           "tag": "时政"
                       }
                   ]
               },
               "items": [
                   {
                       "score": 0.932154,
                       "tag": "科技"
                   },
                   {
                       "score": 0.831555,
                       "tag": "新闻频道"
                   },
                   {
                       "score": 0.802462,
                       "tag": "时政"
                   }
               ],
               "log_id": 3954582346784231900,
               "title": "习近平科技“新语”_新闻频道_央视网(cctv.com)"
           }
    
#### 老版本接口
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


**TODO**
- ~~加token~~
- ~~全局日志记录~~
- ~~重构优化(请求结果格式统一，系统异常处理)~~
- ~~用户中心完善，包含用户分类文章分享功能~~
- ~~文本分类核心算法改用百度的~~
- ~~网络文章操作~~（~~->智能化爬虫~~）
- 完成项目TODO
- 查询分页、排序显示功能
- 批量文章操作
- 用户文章回收站功能
- 文章收藏、点赞、评论功能
- 改用多线程、消息机制处理耗时任务
- 分类算法更替为算法团队那边的
- 删除本项目
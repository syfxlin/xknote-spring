# XK-Note(Spring Boot)

**本项目是为了提交 JavaEE 课设而开发的（虽然之前也是作为体验实习的课设），之前的只是基本的功能，目前已经基本移植完毕，还有很多细节没弄，比如异常处理。之所以重启这个项目一是为 v3 版本做准备，提前踩坑，也顺便熟悉下 Spring（虽然也没用到多少 Spring 的特性），由于我的技术栈已经全部迁移到了 Java，所以 v3 会使用 Java 编写（可能会使用 XK-Java 而不是 Spring，不过 XK-Java 的设计和 Spring 极为类似，迁移其实并不难），顺便还可以作为毕设。**

> 一个集各种神奇功能的云笔记

![Version](https://img.shields.io/github/release/syfxlin/xknote.svg?label=Version&style=flat-square) ![Author](https://img.shields.io/badge/Author-Otstar%20Lin-blue.svg?style=flat-square) ![PHP](https://img.shields.io/badge/php-7.0%2B-green.svg?style=flat-square) ![Lincense](https://img.shields.io/github/license/syfxlin/xknote.svg?style=flat-square)

## 目录 Contents

-   [XK-Note](#xk-note)
    -   [目录 Contents](#%e7%9b%ae%e5%bd%95-contents)
    -   [简介 Introduction](#%e7%ae%80%e4%bb%8b-introduction)
    -   [特性 Feature](#%e7%89%b9%e6%80%a7-feature)
    -   [演示 Demo](#%e6%bc%94%e7%a4%ba-demo)
    -   [安装 Install](#%e5%ae%89%e8%a3%85-install)
        -   [一键安装](#%e4%b8%80%e9%94%ae%e5%ae%89%e8%a3%85)
        -   [升级](#%e5%8d%87%e7%ba%a7)
        -   [手动安装](#%e6%89%8b%e5%8a%a8%e5%ae%89%e8%a3%85)
    -   [文档 Doc](#%e6%96%87%e6%a1%a3-doc)
    -   [维护者 Maintainer](#%e7%bb%b4%e6%8a%a4%e8%80%85-maintainer)
    -   [许可证 License](#%e8%ae%b8%e5%8f%af%e8%af%81-license)
    -   [渲染 Render](#%e6%b8%b2%e6%9f%93-render)

## 简介 Introduction

`XK-Note` = `Laravel` . `Vue2.0` . `XK-Editor`;
一个由上方代码组成，集各种神奇功能的云笔记。

## 特性 Feature

-   [云存储] 云端撰写笔记，随时保存，多端同步。
-   [跨平台] 多平台支持，撰写查阅只需一个浏览器，无惧任何不兼容情况。
-   [响应式] 所有页面均采用响应式设计，即使尺寸极小的设备也能保持良好的体验。
-   [在线浏览] 拥有独立的浏览模式，查看笔记不再困扰。
-   [历史版本] 笔记支持历史版本查看和回滚，您可以切换到任何提交过的历史版本，无惧误删除。(基于 Git)
-   [Git 同步支持] 独有的 Git 支持，支持版本控制，无惧误操作，随时从旧版本恢复笔记。
-   [浏览器临时保存] 独有的浏览器端保存功能，即使断网了也能安心写作，无惧任何网络波动。
-   [发布到博客] 笔记可以在编辑完成后一键推送到 WordPress，Hexo 等博客系统。
-   [多笔记同时打开] 笔记可以随时打开，您无需在编辑其他笔记时关闭之前开启的笔记。
-   [多用户] 笔记主要面向个人使用，但是也支持多人同时使用，每个用户的笔记互相隔离保存，无需担心笔记泄露。
-   [导出笔记] 支持多种导出格式，保存为 MD 文件，html 文件，由本地即时生成，无需繁琐的操作。
-   [多种模式] 拥有多种模式，写作，预览，阅读，满足各种人的需求。
-   还有多种神奇的功能等待你的发掘。

## 演示 Demo

[XK-Note](https://note.ixk.me)

**账号：** demo@ixk.me / demo

**密码：** demodemo

## 安装 Install

> 目前 XK-Note v2 还处于测试阶段，所以可能存在 Bug，若您在使用中遇到了 Bug 或者疑似 Bug 的情况，请提交 issue 或与我取得联系，以便第一时间取得修复。

### 一键安装

1. 安装依赖

```bash
# Ubuntu/Debian 其他系统请自行查阅
# 鉴于不同用户安装PHP的方法不同，这里就不写PHP的安装方法了
sudo apt-get install curl git
curl -sS https://getcomposer.org/installer | php
sudo mv composer.phar /usr/local/bin/composer
# 安装NodeJS和yarn/npm
sudo apt install npm
sudo npm i -g npm
sudo npm i -g yarn
sudo npm i -g n
sudo n stable
```

2. 克隆本仓库

```
git clone https://github.com/syfxlin/xknote.git
```

1. 复制一份`.env.example`文件，并重命名为`.env`，修改对应的信息，并关闭调试模式

```
APP_DEBUG=false
APP_ENV=production
APP_ADMIN_ID=1 #一般来说第一位注册的用户自动升级为管理员，也就是id为1的用户，如果发现不是可以修改这个参数，改成你的id
APP_URL=you url
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=xknote
DB_USERNAME=you mysql username
DB_PASSWORD=you mysql password
MAIL_DRIVER=smtp
MAIL_HOST=smtp.example.com
MAIL_PORT=465
MAIL_USERNAME=you mail username
MAIL_PASSWORD=you main password
MAIL_ENCRYPTION=ssl
MAIL_FROM_ADDRESS=i@example.com
MAIL_FROM_NAME=XK-Note
```

4. 安装

```
composer xknote-install
```

### 升级

> 若您是使用手动安装的话请先确认 git 是否存在 xknote-github 的 remote，如果没有，请添加后运行下方命令

```
composer xknote-update
```

### 手动安装

1. 安装依赖

```bash
# Ubuntu/Debian 其他系统请自行查阅
# 鉴于不同用户安装PHP的方法不同，这里就不写PHP的安装方法了
sudo apt-get install curl git
curl -sS https://getcomposer.org/installer | php
sudo mv composer.phar /usr/local/bin/composer
# 安装NodeJS和yarn/npm
sudo apt install npm
sudo npm i -g npm
sudo npm i -g yarn
sudo npm i -g n
sudo n stable
```

2. 克隆本仓库

```
git clone https://github.com/syfxlin/xknote.git
```

3. 安装模块

```
composer install
yarn
```

4. 复制一份`.env.example`文件，并重命名为`.env`，修改对应的信息，并关闭调试模式，同时运行以下命令生成 app key

```
APP_DEBUG=false
APP_ENV=production
APP_ADMIN_ID=1 #一般来说第一位注册的用户自动升级为管理员，也就是id为1的用户，如果发现不是可以修改这个参数，改成你的id
APP_URL=you url
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=xknote
DB_USERNAME=you mysql username
DB_PASSWORD=you mysql password
MAIL_DRIVER=smtp
MAIL_HOST=smtp.example.com
MAIL_PORT=465
MAIL_USERNAME=you mail username
MAIL_PASSWORD=you main password
MAIL_ENCRYPTION=ssl
MAIL_FROM_ADDRESS=i@example.com
MAIL_FROM_NAME=XK-Note
```

```
php artisan key:generate
```

5. 进入网站根目录，并执行以下命令

```bash
php artisan storage:link
php artisan migrate
php artisan db:seed
```

6. 编译 Vue

```bash
yarn prod
```

7. 修改网站的运行目录到`public`
8. 打开网站，注册一个账户，并确认账户`id`是否为`1`，若不是则需要修改`.env文件`
9. enjoy

## 文档 Doc

暂无

## 维护者 Maintainer

XK-Note 由 [Otstar Lin](https://ixk.me/)和下列[贡献者](https://github.com/syfxlin/xknote/graphs/contributors)的帮助下撰写和维护。

> Otstar Lin - [Personal Website](https://ixk.me/) · [Blog](https://blog.ixk.me/) · [Github](https://github.com/syfxlin)

## 许可证 License

![lincense](https://img.shields.io/github/license/syfxlin/xknote.svg?style=flat-square)

根据 Apache License 2.0 许可证开源。

## 渲染 Render

![ScreenShot-1](https://raw.githubusercontent.com/syfxlin/xknote/master/screenshot-1.png)
![ScreenShot-2](https://raw.githubusercontent.com/syfxlin/xknote/master/screenshot-2.png)
![ScreenShot-3](https://raw.githubusercontent.com/syfxlin/xknote/master/screenshot-3.png)
![ScreenShot-4](https://raw.githubusercontent.com/syfxlin/xknote/master/screenshot-4.png)

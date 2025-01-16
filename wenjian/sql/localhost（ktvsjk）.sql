-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2024-09-11 20:50:36
-- 服务器版本： 5.7.26
-- PHP 版本： 7.3.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `ktvsjk`
--
CREATE DATABASE IF NOT EXISTS `ktvsjk` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `ktvsjk`;

-- --------------------------------------------------------

--
-- 表的结构 `account`
--

CREATE TABLE `account` (
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '账户名',
  `cipher` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '密码'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- 转存表中的数据 `account`
--

INSERT INTO `account` (`name`, `cipher`) VALUES
('maneger', '1'),
('root', '1234'),
('1', '1');

-- --------------------------------------------------------

--
-- 表的结构 `song`
--

CREATE TABLE `song` (
  `uid` int(10) NOT NULL COMMENT '歌曲编号',
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '歌曲名字',
  `singer` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '演唱歌手',
  `language` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '歌曲语言',
  `initials` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '歌曲名缩写',
  `style` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '歌曲风格',
  `address` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '歌曲存放路径'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- 转存表中的数据 `song`
--

INSERT INTO `song` (`uid`, `name`, `singer`, `language`, `initials`, `style`, `address`) VALUES
(1, '达尔文', '林俊杰', '中文', 'dew', '一', 'wenjian\\song\\林俊杰-达尔文.wav'),
(2, '普通朋友', '陶喆', '中文', 'ptpy', '二', 'wenjian\\song\\陶喆-普通朋友.wav'),
(3, '稻香', '周杰伦', '中文', 'dx', '一', 'wenjian\\song\\周杰伦-稻香.wav'),
(4, 'lemon', '米津玄师', '英语', 'lemon', '三', 'wenjian\\song\\米津玄師-Lemon.wav'),
(5, 'Ed Sheeran-Shape of You', 'Ed', '英语', 'ed', '四', 'wenjian\\song\\Ed Sheeran-Shape of You.wav');

-- --------------------------------------------------------

--
-- 表的结构 `yibogequ`
--

CREATE TABLE `yibogequ` (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '歌曲名字',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '歌曲地址'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `yibogequ`
--

INSERT INTO `yibogequ` (`name`, `address`) VALUES
('Ed Sheeran-Shape of You', 'wenjian\\song\\Ed Sheeran-Shape of You.wav'),
('稻香', 'wenjian\\song\\周杰伦-稻香.wav'),
('达尔文', 'wenjian\\song\\林俊杰-达尔文.wav'),
('Ed Sheeran-Shape of You', 'wenjian\\song\\Ed Sheeran-Shape of You.wav'),
('稻香', 'wenjian\\song\\周杰伦-稻香.wav'),
('达尔文', 'wenjian\\song\\林俊杰-达尔文.wav'),
('lemon', 'wenjian\\song\\米津玄師-Lemon.wav');

-- --------------------------------------------------------

--
-- 表的结构 `yidiangequ`
--

CREATE TABLE `yidiangequ` (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '歌曲名字',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '歌曲地址'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `yidiangequ`
--

INSERT INTO `yidiangequ` (`name`, `address`) VALUES
('达尔文', 'wenjian\\song\\林俊杰-达尔文.wav'),
('Ed Sheeran-Shape of You', 'wenjian\\song\\Ed Sheeran-Shape of You.wav'),
('达尔文', 'wenjian\\song\\林俊杰-达尔文.wav'),
('普通朋友', 'wenjian\\song\\陶喆-普通朋友.wav');

--
-- 转储表的索引
--

--
-- 表的索引 `song`
--
ALTER TABLE `song`
  ADD PRIMARY KEY (`uid`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `song`
--
ALTER TABLE `song`
  MODIFY `uid` int(10) NOT NULL AUTO_INCREMENT COMMENT '歌曲编号', AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

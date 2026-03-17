<template>
  <el-container>
    <el-header>
      <el-menu mode="horizontal" router>
        <el-menu-item index="/">上传</el-menu-item>
        <el-menu-item index="/documents">文档</el-menu-item>
        <el-menu-item index="/qa">问答</el-menu-item>
        <el-menu-item index="/history">历史</el-menu-item>
        <el-menu-item index="/summary">摘要</el-menu-item>
        <el-menu-item index="/interview">面试题</el-menu-item>
      </el-menu>
    </el-header>
    <el-main><router-view /></el-main>
  </el-container>
</template>

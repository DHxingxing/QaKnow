<script setup>
import { ElMessage } from 'element-plus'
import { http } from '../api'
const upload = async (param) => {
  const fd = new FormData(); fd.append('file', param.file)
  await http.post('/documents/upload', fd)
  ElMessage.success('上传成功')
}
</script>
<template><el-upload :http-request="upload"><el-button type="primary">上传文档</el-button></el-upload></template>

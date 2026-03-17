<script setup>
import { onMounted, ref } from 'vue'
import { http } from '../api'
const list = ref([])
const load = async()=>{ list.value=(await http.get('/documents')).data.data }
const parse = async(id)=>{ await http.post(`/documents/${id}/parse`); load() }
const indexDoc = async(id)=>{ await http.post(`/documents/${id}/index`); load() }
onMounted(load)
</script>
<template><el-table :data="list"><el-table-column prop="id" label="ID"/><el-table-column prop="name" label="名称"/><el-table-column prop="parseStatus" label="状态"/><el-table-column label="操作"><template #default="s"><el-button @click="parse(s.row.id)">解析</el-button><el-button type="primary" @click="indexDoc(s.row.id)">索引</el-button></template></el-table-column></el-table></template>

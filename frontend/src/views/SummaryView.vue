<script setup>
import { ref } from 'vue';import { http } from '../api';const docId=ref('');const result=ref('');const gen=async()=>{result.value=(await http.post(`/documents/${docId.value}/summary`)).data.data.summaryText}
</script>
<template><el-input v-model="docId" placeholder="文档ID"/><el-button @click="gen">生成摘要</el-button><el-card>{{result}}</el-card></template>

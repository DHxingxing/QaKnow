<script setup>
import { ref } from 'vue';import { http } from '../api';const docId=ref('');const list=ref([]);const gen=async()=>{list.value=(await http.post(`/documents/${docId.value}/interview-questions`)).data.data}
</script>
<template><el-input v-model="docId" placeholder="文档ID"/><el-button @click="gen">生成面试题</el-button><el-card v-for="i in list" :key="i.id">{{i.questionText}}<br/>{{i.answerText}}</el-card></template>

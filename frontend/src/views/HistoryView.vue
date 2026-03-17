<script setup>
import { onMounted, ref } from 'vue';import { http } from '../api';const list=ref([]);onMounted(async()=>list.value=(await http.get('/qa/history')).data.data)
</script>
<template><el-timeline><el-timeline-item v-for="i in list" :key="i.id" :timestamp="i.createdAt"><b>{{i.question}}</b><p>{{i.answer}}</p></el-timeline-item></el-timeline></template>

<script setup>
import { ref } from 'vue'
import { http } from '../api'
const question = ref('')
const answer = ref('')
const citations = ref([])
const ask = async()=>{ const res=(await http.post('/qa/ask',{question:question.value})).data.data; answer.value=res.answer; citations.value=res.citations }
</script>
<template><el-input v-model="question" placeholder="请输入问题"/><el-button type="primary" @click="ask">提问</el-button><el-card>{{answer}}</el-card><el-card v-for="(c,i) in citations" :key="i">[{{i+1}}] {{c.documentName}} / {{c.chapterTitle}} / chunk {{c.chunkOrder}}<br/>{{c.snippet}}</el-card></template>

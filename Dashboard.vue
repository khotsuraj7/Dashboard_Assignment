
<template>
  <div>
    <div v-if="loading">Loading sellers...</div>
    <div v-else>
      <select v-model="selectedId" @change="fetchSummary">
        <option v-for="s in sellers" :key="s.id" :value="s.id">{{ s.name }}</option>
      </select>
      <button @click="fetchSummary">Refresh</button>

      <div v-if="summary">
        <h2>{{ summary.sellerName }}</h2>
        <p>Total Sales: {{ summary.totalSales }}</p>
        <p>Total Revenue: {{ summary.totalRevenue }}</p>
        <p>Return Rate: {{ (summary.returnRate * 100).toFixed(2) }}%</p>
        <div v-if="summary.alerts && summary.alerts.length">
          <h3>Alerts</h3>
          <ul>
            <li v-for="a in summary.alerts" :key="a">{{ a }}</li>
          </ul>
        </div>
      </div>

      <div v-if="error" style="color:red">{{ error }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const sellers = ref([])
const selectedId = ref(null)
const summary = ref(null)
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  try {
    const res = await axios.get('/api/seller/list')
    sellers.value = res.data
    if (sellers.value.length) {
      selectedId.value = sellers.value[0].id
      await fetchSummary()
    }
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
})

async function fetchSummary() {
  if (!selectedId.value) return
  summary.value = null
  error.value = null
  try {
    const res = await axios.get(`/api/seller/${selectedId.value}/summary`)
    summary.value = res.data
  } catch (e) {
    error.value = e.response?.data?.message || e.message
  }
}
</script>

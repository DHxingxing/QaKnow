import UploadView from './views/UploadView.vue'
import DocumentListView from './views/DocumentListView.vue'
import QaView from './views/QaView.vue'
import HistoryView from './views/HistoryView.vue'
import SummaryView from './views/SummaryView.vue'
import InterviewView from './views/InterviewView.vue'

export const routes = [
  { path: '/', component: UploadView },
  { path: '/documents', component: DocumentListView },
  { path: '/qa', component: QaView },
  { path: '/history', component: HistoryView },
  { path: '/summary', component: SummaryView },
  { path: '/interview', component: InterviewView }
]

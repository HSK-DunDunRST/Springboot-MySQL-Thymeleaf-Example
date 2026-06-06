// 공통 JavaScript 기능들

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    // 툴팁 초기화 (Bootstrap)
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // 모든 카드에 페이드인 애니메이션 적용
    const cards = document.querySelectorAll('.card, .question-card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('fade-in');
    });

    // 폼 자동 저장 기능 (로컬 스토리지 사용)
    setupAutoSave();

    // 검색 기능 개선
    setupSearchEnhancements();

    // 답변 작성 시 실시간 글자 수 표시
    setupCharacterCount();
});

// 자동 저장 기능
function setupAutoSave() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        const textareas = form.querySelectorAll('textarea');
        const inputs = form.querySelectorAll('input[type="text"]');
        
        [...textareas, ...inputs].forEach(element => {
            const storageKey = `autosave_${element.id || element.name}`;
            
            // 저장된 내용 복원
            const savedContent = localStorage.getItem(storageKey);
            if (savedContent && !element.value) {
                element.value = savedContent;
            }
            
            // 자동 저장
            element.addEventListener('input', debounce(() => {
                if (element.value.trim()) {
                    localStorage.setItem(storageKey, element.value);
                } else {
                    localStorage.removeItem(storageKey);
                }
            }, 1000));
            
            // 폼 제출 시 자동 저장 데이터 삭제
            form.addEventListener('submit', () => {
                localStorage.removeItem(storageKey);
            });
        });
    });
}

// 검색 기능 개선
function setupSearchEnhancements() {
    const searchInput = document.querySelector('input[name="kw"]');
    if (searchInput) {
        // 검색어 하이라이트
        const searchTerm = searchInput.value.trim();
        if (searchTerm) {
            highlightSearchTerms(searchTerm);
        }

        // 검색 입력 시 실시간 검색 제안
        searchInput.addEventListener('input', debounce(() => {
            const value = searchInput.value.trim();
            if (value.length > 1) {
                // 여기에 실시간 검색 제안 로직 추가 가능
                console.log('검색어:', value);
            }
        }, 300));
    }
}

// 검색어 하이라이트
function highlightSearchTerms(searchTerm) {
    const questionTitles = document.querySelectorAll('.question-title');
    questionTitles.forEach(title => {
        const text = title.textContent;
        const highlightedText = text.replace(
            new RegExp(`(${escapeRegExp(searchTerm)})`, 'gi'),
            '<mark class="bg-warning">$1</mark>'
        );
        if (highlightedText !== text) {
            title.innerHTML = highlightedText;
        }
    });
}

// 글자 수 카운터
function setupCharacterCount() {
    const textareas = document.querySelectorAll('textarea');
    textareas.forEach(textarea => {
        const maxLength = textarea.getAttribute('maxlength') || 1000;
        
        // 글자 수 표시 요소 생성
        const countElement = document.createElement('div');
        countElement.className = 'text-muted small mt-1 text-end';
        countElement.innerHTML = `<span class="current">0</span> / ${maxLength}자`;
        
        textarea.parentNode.appendChild(countElement);
        
        // 실시간 글자 수 업데이트
        textarea.addEventListener('input', () => {
            const current = textarea.value.length;
            const currentSpan = countElement.querySelector('.current');
            currentSpan.textContent = current;
            
            // 글자 수에 따른 색상 변경
            if (current > maxLength * 0.9) {
                countElement.className = 'text-danger small mt-1 text-end';
            } else if (current > maxLength * 0.7) {
                countElement.className = 'text-warning small mt-1 text-end';
            } else {
                countElement.className = 'text-muted small mt-1 text-end';
            }
        });
        
        // 초기 글자 수 설정
        textarea.dispatchEvent(new Event('input'));
    });
}

// 유틸리티 함수들

// 디바운스 함수
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// 정규식 이스케이프
function escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

// 스크롤 애니메이션
function smoothScrollTo(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
    }
}

// 확인 대화상자 개선
function confirmAction(message, callback) {
    if (confirm(message)) {
        if (typeof callback === 'function') {
            callback();
        }
        return true;
    }
    return false;
}

// 성공 메시지 표시
function showSuccessMessage(message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-success alert-dismissible fade show position-fixed';
    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    alertDiv.innerHTML = `
        <i class="fas fa-check-circle me-2"></i>${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    // 3초 후 자동 제거
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 3000);
}

// 로딩 스피너 표시/숨김
function showLoading() {
    const loadingDiv = document.createElement('div');
    loadingDiv.id = 'loading-spinner';
    loadingDiv.className = 'position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center';
    loadingDiv.style.cssText = 'background: rgba(0,0,0,0.5); z-index: 9999;';
    loadingDiv.innerHTML = `
        <div class="text-center text-white">
            <div class="spinner-border text-primary mb-3" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <div>처리 중...</div>
        </div>
    `;
    
    document.body.appendChild(loadingDiv);
}

function hideLoading() {
    const loadingDiv = document.getElementById('loading-spinner');
    if (loadingDiv) {
        loadingDiv.remove();
    }
}

// 폼 제출 시 로딩 표시
document.addEventListener('submit', function(e) {
    if (e.target.tagName === 'FORM') {
        showLoading();
        
        // 에러 발생 시 로딩 숨김
        setTimeout(() => {
            hideLoading();
        }, 10000); // 10초 후 자동 숨김
    }
});

// 페이지 언로드 시 로딩 숨김
window.addEventListener('beforeunload', function() {
    hideLoading();
});

// 뒤로가기 버튼 기능 개선
function goBack() {
    if (window.history.length > 1) {
        window.history.back();
    } else {
        window.location.href = '/question/list';
    }
}

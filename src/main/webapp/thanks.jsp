<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Survey Confirmation</title>
  <link rel="stylesheet" href="styles/main.css">
</head>
<body>



  <main class="container animate-fadeIn delay-1">
    <div class="success-header">
      <div class="checkmark-wrapper">
        <svg class="checkmark-svg" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
          <circle class="checkmark-circle" cx="26" cy="26" fill="none"/>
          <path class="checkmark-check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
        </svg>
      </div>
      <h1>Thanks for filling out our survey!</h1>
      <p class="intro-text" style="margin-bottom: 0;">Here is the information you provided:</p>
    </div>

    <div class="confirmation-card animate-fadeIn delay-2" id="confirmationContent">
      <div class="card-subtitle">Personal Details</div>
      <table class="info-table">
        <tr>
          <td class="label">First Name:</td>
          <td class="value" id="resFirstName">—</td>
        </tr>
        <tr>
          <td class="label">Last Name:</td>
          <td class="value" id="resLastName">—</td>
        </tr>
        <tr>
          <td class="label">Email:</td>
          <td class="value" id="resEmail">—</td>
        </tr>
        <tr>
          <td class="label">Date of Birth:</td>
          <td class="value" id="resDob">—</td>
        </tr>
      </table>

      <div class="card-subtitle" style="margin-top: 18px;">Preferences & Feedback</div>
      <table class="info-table">
        <tr>
          <td class="label">Heard from:</td>
          <td class="value" id="resSource">—</td>
        </tr>
        <tr>
          <td class="label">Announcements:</td>
          <td class="value" id="resAnnounce">—</td>
        </tr>
        <tr>
          <td class="label">Email Updates:</td>
          <td class="value" id="resEmailUpdates">—</td>
        </tr>
        <tr>
          <td class="label">Contact by:</td>
          <td class="value" id="resContact">—</td>
        </tr>
      </table>
    </div>

    <div class="note-box animate-fadeIn delay-3">
      To enter another survey, click on the Back button in your browser or the Return button shown below.
    </div>

    <div class="button-group animate-fadeIn delay-4">
      <button class="btn" onclick="window.location.href='index.jsp'">
        <span>Return</span>
        <svg class="btn-arrow-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <line x1="5" y1="12" x2="19" y2="12"></line>
          <polyline points="12 5 19 12 12 19"></polyline>
        </svg>
      </button>
    </div>
  </main>

  <script>
    // Retrieve and safely render stored survey data
    const rawData = localStorage.getItem('surveyData');

    if (rawData) {
      try {
        const data = JSON.parse(rawData);

        // Escape HTML to prevent XSS
        function escapeHtml(str) {
          if (!str) return '—';
          return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
        }

        document.getElementById('resFirstName').textContent = data.firstName || '—';
        document.getElementById('resLastName').textContent = data.lastName || '—';
        document.getElementById('resEmail').textContent = data.email || '—';
        document.getElementById('resDob').textContent = data.dob || '—';
        
        // Format heard from source
        const sourceEl = document.getElementById('resSource');
        if (data.source) {
          sourceEl.innerHTML = '<span class="badge badge-source">' + escapeHtml(data.source) + '</span>';
        } else {
          sourceEl.textContent = '—';
        }

        // Format announcements badge
        const isAnnounce = (data.announcements === 'Yes');
        document.getElementById('resAnnounce').innerHTML = 
          '<span class="badge ' + (isAnnounce ? 'badge-yes' : 'badge-no') + '">' + (isAnnounce ? 'Yes' : 'No') + '</span>';

        // Format email announcements badge
        const isEmailUpdates = (data.emailAnnouncements === 'Yes');
        document.getElementById('resEmailUpdates').innerHTML = 
          '<span class="badge ' + (isEmailUpdates ? 'badge-yes' : 'badge-no') + '">' + (isEmailUpdates ? 'Yes' : 'No') + '</span>';

        document.getElementById('resContact').textContent = data.contactMethod || '—';
      } catch (e) {
        console.error('Error parsing survey data', e);
      }
    } else {
      document.getElementById('confirmationContent').innerHTML = 
        '<p style="color: #666; margin: 15px 0;">No survey submission found. Please fill out the survey first.</p>';
    }

    // Interactive 3D Perspective Card Tilt
    const card = document.querySelector('.container');
    if (card && window.matchMedia('(pointer: fine)').matches) {
      card.addEventListener('mousemove', (e) => {
        const rect = card.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        const centerX = rect.width / 2;
        const centerY = rect.height / 2;
        const rotateX = ((y - centerY) / centerY) * -2.5;
        const rotateY = ((x - centerX) / centerX) * 2.5;
        card.style.transform = 'perspective(1200px) rotateX(' + rotateX + 'deg) rotateY(' + rotateY + 'deg)';
      });

      card.addEventListener('mouseleave', () => {
        card.style.transform = 'perspective(1200px) rotateX(0deg) rotateY(0deg)';
      });
    }
  </script>
</body>
</html>
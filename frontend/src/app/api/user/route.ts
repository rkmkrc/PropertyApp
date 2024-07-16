import { NextResponse } from 'next/server';
import { cookies } from 'next/headers';

export async function GET() {
  const cookieStore = cookies();
  const token = cookieStore.get('token')?.value;

  if (!token) {
    return NextResponse.json({ success: false, message: 'No token found' }, { status: 401 });
  }

  const backendUserUrl = `http://localhost:8080/api/v1/user`;

  const response = await fetch(backendUserUrl, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  const data = await response.json();

  if (response.ok) {
    return NextResponse.json({ success: true, user: data });
  } else {
    return NextResponse.json({ success: false, message: data.message || 'Failed to fetch user data' }, { status: response.status });
  }
}

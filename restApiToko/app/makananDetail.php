<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class makananDetail extends Model
{
    protected $primaryKey = 'makanan_detail_id';
    public function makananMaster(){
        return $this->belongsTo('App\makanan','makanan_id','makanan_id');
    }

    public function bahanPokok(){
        return $this->belongsTo('App\bahanPokok','bahan_pokok_id','bahan_pokok_id');
    }
}
